package ru.itsyn.cuba.menu_editor.web.menu_item_entity;

import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

@UiController("menu_MenuItemEntity.edit")
@UiDescriptor("menu-item-entity-edit.xml")
@EditedEntityContainer("editDc")
@LoadDataBeforeShow
public class MenuItemEntityEditor extends StandardEditor<MenuItemEntity> {
}