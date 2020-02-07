package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;

@UiController("menu_MenuEntity.browse")
@UiDescriptor("menu-browse.xml")
@LookupComponent("table")
@LoadDataBeforeShow
public class MenuBrowser extends StandardLookup<MenuEntity> {
}