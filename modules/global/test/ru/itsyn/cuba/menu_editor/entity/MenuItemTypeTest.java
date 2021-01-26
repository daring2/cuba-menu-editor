package ru.itsyn.cuba.menu_editor.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MenuItemTypeTest {

    @Test
    public void testFromId() {
        assertNull(MenuItemType.fromId(null));
        assertNull(MenuItemType.fromId("NONE"));
        assertEquals(MenuItemType.MENU, MenuItemType.fromId("MENU"));
    }

}