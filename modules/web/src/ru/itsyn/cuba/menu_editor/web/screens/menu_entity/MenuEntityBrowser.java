package ru.itsyn.cuba.menu_editor.web.screens.menu_entity;

import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;

@UiController("menu_MenuEntity.browse")
@UiDescriptor("menu-entity-browse.xml")
@LookupComponent("table")
@LoadDataBeforeShow
public class MenuEntityBrowser extends StandardLookup<MenuEntity> {
}