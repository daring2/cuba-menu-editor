package ru.itsyn.cuba.menu_editor.web.menu_config;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.config.MenuConfig;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class BaseMenuConfig extends MenuConfig {

    @Inject
    protected DataManager dataManager;

    @Override
    protected void init() {
        var defMenu = loadDefaultMenu();
        if (defMenu != null) {
            rootItems.clear();
            var d = dom4JTools.readDocument(defMenu.getConfig());
            loadMenuItems(d.getRootElement(), null);
            return;
        }
        super.init();
    }

    @Nullable
    protected MenuEntity loadDefaultMenu() {
        var query = "select e from menu_MenuEntity e" +
                " where e.code = :code";
        return dataManager.load(MenuEntity.class)
                .query(query)
                .parameter("code", MenuEntity.DEFAULT_CODE)
                .optional()
                .orElse(null);
    }

}
