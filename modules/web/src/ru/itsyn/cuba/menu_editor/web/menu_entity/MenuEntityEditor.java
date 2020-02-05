package ru.itsyn.cuba.menu_editor.web.menu_entity;

import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("menu_MenuEntity.edit")
@UiDescriptor("menu-entity-edit.xml")
@EditedEntityContainer("editDc")
@LoadDataBeforeShow
public class MenuEntityEditor extends StandardEditor<MenuEntity> {

    @Inject
    MenuItemLoader menuItemLoader;

    @Install(to = "menuDl", target = Target.DATA_LOADER)
    private List<MenuItemEntity> loadMenu(LoadContext<MenuItemEntity> lc) {
        var items = new ArrayList<MenuItemEntity>();
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        rootItem.visitItems(items::add);
        return items;
    }

}