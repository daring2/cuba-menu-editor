package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.model.CollectionContainer;
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

    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    private List<MenuItemEntity> loadMenuItems(LoadContext<MenuItemEntity> lc) {
        var items = new ArrayList<MenuItemEntity>();
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        rootItem.visitItems(items::add);
        return items;
    }

    @Subscribe("itemsTable.edit")
    void onItemEdit(Action.ActionPerformedEvent event) {
        screenBuilders.editor(itemsTable)
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .show();
    }

    @Subscribe
    void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        var items = itemsDc.getItems();
        if (items.isEmpty()) return;
        var rootItem = items.get(0);
        var doc = menuConfigBuilder.buildMenuConfig(rootItem.getChildren());
        var config = Dom4j.writeDocument(doc, true);
        getEditedEntity().setConfig(config);
    }

}