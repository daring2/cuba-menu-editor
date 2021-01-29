package ru.itsyn.cuba.menu_editor.web.menu_item;

import org.junit.jupiter.api.Test;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuConfigBuilderTest {

    @Test
    void testGetElementName() {
        var builder = new MenuConfigBuilder();
        assertEquals("menu", builder.getElementName(MenuItemType.MENU));
        assertEquals("separator", builder.getElementName(MenuItemType.SEPARATOR));
        assertEquals("item", builder.getElementName(MenuItemType.SCREEN));
        assertEquals("item", builder.getElementName(null));
    }

}