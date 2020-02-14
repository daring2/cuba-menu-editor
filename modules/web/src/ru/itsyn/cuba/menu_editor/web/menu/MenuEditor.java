package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.model.CollectionChangeType;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionContainer.CollectionChangeEvent;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;
import ru.itsyn.cuba.menu_editor.web.menu_item.MenuConfigBuilder;
import ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemLoader;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemFactory.ROOT_ITEM_ID;

@UiController("menu_MenuEntity.edit")
@UiDescriptor("menu-edit.xml")
@EditedEntityContainer("editDc")
@LoadDataBeforeShow
public class MenuEditor extends StandardEditor<MenuEntity> {

    @Inject
    ScreenBuilders screenBuilders;
    @Inject
    MenuItemLoader menuItemLoader;
    @Inject
    MenuConfigBuilder menuConfigBuilder;
    @Inject
    DataContext dataContext;
    @Inject
    CollectionContainer<MenuItemEntity> itemsDc;
    @Inject
    CollectionLoader<MenuItemEntity> itemsDl;
    @Inject
    TreeTable<MenuItemEntity> itemsTable;

    //TODO add rule for remove action

    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    private List<MenuItemEntity> loadMenuItems(LoadContext<MenuItemEntity> lc) {
        var items = new ArrayList<MenuItemEntity>();
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        rootItem.visitItems(items::add);
        return items;
    }

    @Subscribe("itemsTable.create")
    void onItemCreate(ActionPerformedEvent event) {
        var si = itemsTable.getSingleSelected();
        if (si == null) si = getRootItem();
        var parent = si.isMenu() ? si : si.getParent();
        var index = parent != si ? parent.getChildIndex(si) + 1 : 0;
        //TODO apply index for table
        screenBuilders.editor(itemsTable)
                .newEntity()
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withInitializer(i -> i.setParent(parent))
                .withTransformation(i -> {
                    parent.addChild(i, index);
                    return i;
                })
                .show();
    }

    @Subscribe("itemsTable.edit")
    void onItemEdit(ActionPerformedEvent event) {
        screenBuilders.editor(itemsTable)
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .show();
    }

    @Subscribe(id = "itemsDc", target = Target.DATA_CONTAINER)
    public void onItemsChange(CollectionChangeEvent<MenuItemEntity> event) {
        if (event.getChangeType() == CollectionChangeType.REMOVE_ITEMS) {
            event.getChanges().forEach(this::onItemRemove);
        }
    }

    void onItemRemove(MenuItemEntity item) {
        item.getParent().removeChild(item);
        var items = itemsDc.getMutableItems();
        //TODO remove children
        // item.visitItems(items::remove);
    }

    @Subscribe
    void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        var rootItem = getRootItem();
        if (rootItem == null) return;
        var doc = menuConfigBuilder.buildMenuConfig(rootItem.getChildren());
        var config = Dom4j.writeDocument(doc, true);
        getEditedEntity().setConfig(config);
    }

    MenuItemEntity getRootItem() {
        return itemsDc.getItem(ROOT_ITEM_ID);
    }

}