package ru.itsyn.cuba.menu_editor.web.menu_config;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.MenuItem;

import javax.inject.Inject;
import java.util.List;

import static ru.itsyn.cuba.menu_editor.web.util.UserSessionUtils.getUserSession;

public class MainMenuConfig extends MenuConfig {

    static final String CONFIG_ATTRIBUTE = "menuConfig";

    @Inject
    protected BeanLocator beanLocator;
    @Inject
    protected UserSessionSource userSessionSource;

    @Override
    public void reset() {
        super.reset();
        //TODO reset all configs
        var session = getUserSession(userSessionSource);
        if (session != null)
            session.removeLocalAttribute(CONFIG_ATTRIBUTE);
    }

    @Override
    public List<MenuItem> getRootItems() {
        var userConfig = getUserMenuConfig();
        if (userConfig != null)
            return userConfig.getRootItems();
        return super.getRootItems();
    }

    protected MenuConfig getUserMenuConfig() {
        var session = getUserSession(userSessionSource);
        if (session == null)
            return null;
        var config = (MenuConfig) session.getLocalAttribute(CONFIG_ATTRIBUTE);
        if (config == null) {
            config = beanLocator.getPrototype(UserMenuConfig.NAME);
            session.setLocalAttribute(CONFIG_ATTRIBUTE, config);
        }
        return config;
    }

}
