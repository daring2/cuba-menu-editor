package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;

import javax.inject.Inject;

@UiController("menu_MenuEntity.browse")
@UiDescriptor("menu-browse.xml")
@LookupComponent("table")
@LoadDataBeforeShow
public class MenuBrowser extends StandardLookup<MenuEntity> {

    @Inject
    protected AppMenuManager appMenuManager;

    @Subscribe("table.apply")
    public void onMenuApply(ActionPerformedEvent event) {
        appMenuManager.reloadAppMenu();
    }

}