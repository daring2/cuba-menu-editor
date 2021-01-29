package ru.itsyn.cuba.menu_editor.web.util;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static ru.itsyn.cuba.menu_editor.web.util.UserSessionUtils.getUserSession;

class UserSessionUtilsTest {

    @Test
    public void testGetUserSession() {
        var sessionSource = mock(UserSessionSource.class);
        when(sessionSource.checkCurrentUserSession()).thenReturn(false);
        assertNull(getUserSession(sessionSource));
        verify(sessionSource, never()).getUserSession();

        when(sessionSource.checkCurrentUserSession()).thenReturn(true);
        var session1 = mock(UserSession.class);
        when(sessionSource.getUserSession()).thenReturn(session1);
        assertSame(session1, getUserSession(sessionSource));
    }

}