package ru.itsyn.cuba.menu_editor.web.screens.menu_entity;

import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;

@UiController("menu_MenuEntity.edit")
@UiDescriptor("menu-entity-edit.xml")
@EditedEntityContainer("menuEntityDc")
@LoadDataBeforeShow
public class MenuEntityEditor extends StandardEditor<MenuEntity> {
}