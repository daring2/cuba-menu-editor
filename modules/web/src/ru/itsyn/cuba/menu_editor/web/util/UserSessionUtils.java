package ru.itsyn.cuba.menu_editor.web.util;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;

public class UserSessionUtils {

    @Nullable
    public static UserSession getUserSession(UserSessionSource source) {
        if (!source.checkCurrentUserSession())
            return null;
        return source.getUserSession();
    }

    private UserSessionUtils() {
    }

}
