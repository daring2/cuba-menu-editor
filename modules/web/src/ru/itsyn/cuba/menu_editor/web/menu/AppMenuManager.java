package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.components.mainwindow.SideMenu;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.web.AppUI;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(AppMenuManager.NAME)
public class AppMenuManager {

    public static final String NAME = "menu_AppMenuManager";

    @Inject
    protected MenuConfig menuConfig;

    public void reloadAppMenu() {
        menuConfig.reset();
        var topWindow = AppUI.getCurrent().getTopLevelWindowNN();
        var appMenu = (AppMenu) ObjectUtils.firstNonNull(
                topWindow.getComponent("appMenu"),
                topWindow.getComponent("mainMenu")
        );
        if (appMenu != null) {
            removeAllMenuItems(appMenu);
            appMenu.loadMenu();
        }
        var sideMenu = (SideMenu) topWindow.getComponent("sideMenu");
        if (sideMenu != null) {
            sideMenu.removeAllMenuItems();
            sideMenu.loadMenuConfig();
        }
    }

    protected void removeAllMenuItems(AppMenu menu) {
        for (var mi : menu.getMenuItems()) {
            removeMenuItem(mi);
            menu.removeMenuItem(mi);
        }
    }

    protected void removeMenuItem(AppMenu.MenuItem menuItem) {
        for (var ci : menuItem.getChildren()) {
            removeMenuItem(ci);
            menuItem.removeChildItem(ci);
        }
    }

}
