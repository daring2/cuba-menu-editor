package ru.itsyn.cuba.menu_editor.web.menu_config;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.config.MenuConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static ru.itsyn.cuba.menu_editor.web.util.UserSessionUtils.getUserSession;

@Component(UserMenuConfig.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserMenuConfig extends MenuConfig {

    public static final String NAME = "cuba_UserMenuConfig";

    @Inject
    protected DataManager dataManager;
    @Inject
    protected UserSessionSource userSessionSource;

    @Override
    protected void init() {
        var userMenu = loadMenuEntity();
        if (userMenu != null) {
            rootItems.clear();
            var d = dom4JTools.readDocument(userMenu.getConfig());
            loadMenuItems(d.getRootElement(), null);
            return;
        }
        super.init();
    }

    @Nullable
    protected MenuEntity loadMenuEntity() {
        var session = getUserSession(userSessionSource);
        if (session == null)
            return null;
        var user = session.getCurrentOrSubstitutedUser();
        var query = "select e from menu_MenuEntity e" +
                " where e.role in (select r.role from sec$UserRole r where r.user.id = :userId)" +
                " order by e.priority desc";
        return dataManager.load(MenuEntity.class)
                .query(query)
                .parameter("userId", user.getId())
                .maxResults(1)
                .optional()
                .orElse(null);
    }

}
