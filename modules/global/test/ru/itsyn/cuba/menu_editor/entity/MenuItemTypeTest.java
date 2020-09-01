package ru.itsyn.cuba.menu_editor.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MenuItemTypeTest {

    @Test
    public void testFromId() {
        assertNull(MenuItemType.fromId(null));
        assertNull(MenuItemType.fromId("NONE"));
        assertEquals(MenuItemType.MENU, MenuItemType.fromId("MENU"));
    }

}