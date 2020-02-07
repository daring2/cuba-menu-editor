package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

@UiController("menu_MenuItemEntity.edit")
@UiDescriptor("menu-item-edit.xml")
@EditedEntityContainer("editDc")
@LoadDataBeforeShow
public class MenuItemEditor extends StandardEditor<MenuItemEntity> {
}