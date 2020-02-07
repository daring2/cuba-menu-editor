package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

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
    TreeTable<MenuItemEntity> menuTable;

    @Install(to = "menuDl", target = Target.DATA_LOADER)
    private List<MenuItemEntity> loadMenu(LoadContext<MenuItemEntity> lc) {
        var items = new ArrayList<MenuItemEntity>();
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        rootItem.visitItems(items::add);
        return items;
    }

    @Subscribe("menuTable.edit")
    public void onItemEdit(Action.ActionPerformedEvent event) {
        screenBuilders.editor(menuTable)
                .withOpenMode(OpenMode.DIALOG)
                .show();
    }

}