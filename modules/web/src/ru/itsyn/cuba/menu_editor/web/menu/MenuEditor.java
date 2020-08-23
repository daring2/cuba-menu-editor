package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.RemoveOperation;
import com.haulmont.cuba.gui.RemoveOperation.AfterActionPerformedEvent;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.RemoveAction;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.TreeDataGrid;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionContainer.CollectionChangeEvent;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.shared.ui.grid.DropLocation;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropEvent;
import com.vaadin.ui.components.grid.TreeGridDropTarget;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;
import ru.itsyn.cuba.menu_editor.web.menu_item.MenuConfigBuilder;
import ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemLoader;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static com.haulmont.cuba.gui.model.CollectionChangeType.ADD_ITEMS;
import static com.vaadin.shared.ui.dnd.DragSourceState.DATA_TYPE_TEXT_PLAIN;
import static ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemFactory.ROOT_ITEM_ID;
import static ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemUtils.buildItemList;
import static ru.itsyn.cuba.menu_editor.web.util.DialogUtils.newConfirmationDialog;

@UiController("menu_MenuEntity.edit")
@UiDescriptor("menu-edit.xml")
@EditedEntityContainer("editDc")
@LoadDataBeforeShow
public class MenuEditor extends StandardEditor<MenuEntity> {

    @Inject
    ScreenBuilders screenBuilders;
    @Inject
    Notifications notifications;
    @Inject
    RemoveOperation removeOperation;
    @Inject
    MenuItemLoader menuItemLoader;
    @Inject
    MenuConfigBuilder menuConfigBuilder;
    @Inject
    DataContext dataContext;
    @Inject
    MessageBundle messageBundle;
    @Inject
    CollectionContainer<MenuItemEntity> itemsDc;
    @Inject
    CollectionLoader<MenuItemEntity> itemsDl;
    @Inject
    TreeDataGrid<MenuItemEntity> itemsTable;
    @Named("itemsTable.remove")
    RemoveAction<MenuItemEntity> itemRemoveAction;

    @Subscribe
    public void onInit(InitEvent event) {
        initItemDragAndDrop();
        initRemoveItemAction();
    }

    void initItemDragAndDrop() {
        TreeGrid<MenuItemEntity> grid = itemsTable.unwrap(TreeGrid.class);
        var dragSource = new TreeGridDragSource<>(grid);
        dragSource.setEffectAllowed(EffectAllowed.MOVE);
        dragSource.setDragDataGenerator(DATA_TYPE_TEXT_PLAIN, MenuItemEntity::getId);
        var dropTarget = new TreeGridDropTarget<>(grid, DropMode.ON_TOP_OR_BETWEEN);
        //TODO add DropCriteriaScript
        dropTarget.addTreeGridDropListener(this::onDropItem);
    }

    void onDropItem(TreeGridDropEvent<MenuItemEntity> event) {
        var item = event.getDataTransferData(DATA_TYPE_TEXT_PLAIN)
                .map(itemsDc::getItemOrNull)
                .orElse(null);
        if (item == null || getRootItem().equals(item))
            return;
        var targetItem = event.getDropTargetRow().orElse(null);
        if (targetItem == null)
            return;
        var dropLoc = event.getDropLocation();
        if (dropLoc == DropLocation.ON_TOP && targetItem.isMenu()) {
            moveItem(item, targetItem, 0);
        } else {
            var parent = targetItem.getParent();
            var index = parent.getChildIndex(targetItem);
            if (dropLoc != DropLocation.ABOVE)
                index += 1;
            moveItem(item, parent, index);
        }
    }

    void moveItem(MenuItemEntity item, MenuItemEntity parent, int index) {
        if (parent.equals(item) || parent.hasAncestor(item)) {
            var warning = messageBundle.getMessage("cyclicDependencyWarning");
            notifications.create()
                    .withCaption(warning)
                    .show();
            return;
        }
        var pi = item.getParent();
        if (pi == parent && pi.getChildIndex(item) < index)
            index -= 1;
        pi.removeChild(item);
        parent.addChild(item, index);
        refreshItems();
    }

    void initRemoveItemAction() {
        itemRemoveAction.addEnabledRule(() -> {
            var items = itemsTable.getSelected();
            return !items.contains(getRootItem());
        });
    }

    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    List<MenuItemEntity> loadMenuItems(LoadContext<MenuItemEntity> lc) {
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        return buildItemList(rootItem);
    }

    @Subscribe(id = "itemsDc", target = Target.DATA_CONTAINER)
    public void onItemsChange(CollectionChangeEvent<MenuItemEntity> event) {
        if (event.getChangeType() == ADD_ITEMS)
            refreshItems();
    }

    void refreshItems() {
        var rootItem = getRootItem();
        itemsDc.setItems(buildItemList(rootItem));
    }

    @Subscribe("itemsTable.create")
    void onItemCreate(ActionPerformedEvent event) {
        var si = itemsTable.getSingleSelected();
        if (si == null) si = getRootItem();
        var parent = si.isMenu() ? si : si.getParent();
        var index = parent != si ? parent.getChildIndex(si) + 1 : 0;
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

    @Subscribe("itemsTable.remove")
    void onItemRemove(ActionPerformedEvent event) {
        removeOperation.builder(itemsTable)
                .afterActionPerformed(this::afterItemRemove)
                .remove();
    }

    void afterItemRemove(AfterActionPerformedEvent<MenuItemEntity> event) {
        var items = itemsDc.getMutableItems();
        for (MenuItemEntity item : event.getItems()) {
            item.getParent().removeChild(item);
            item.visitItems(items::remove);
        }
    }

    @Subscribe("itemsTable.resetMenu")
    void onResetMenu(ActionPerformedEvent event) {
        newConfirmationDialog(
                messageBundle.getMessage("resetConfirmation"),
                this::resetMenu
        ).show();
    }

    void resetMenu(ActionPerformedEvent event) {
        var rootItem = menuItemLoader.loadDefaultMenu();
        updateMenuConfig(rootItem);
        itemsDl.load();
    }

    @Subscribe
    void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        var rootItem = getRootItem();
        if (rootItem == null) return;
        updateMenuConfig(rootItem);
    }

    void updateMenuConfig(MenuItemEntity rootItem) {
        var doc = menuConfigBuilder.buildMenuConfig(rootItem.getChildren());
        var config = Dom4j.writeDocument(doc, true);
        getEditedEntity().setConfig(config);
    }

    MenuItemEntity getRootItem() {
        return itemsDc.getItem(ROOT_ITEM_ID);
    }

}