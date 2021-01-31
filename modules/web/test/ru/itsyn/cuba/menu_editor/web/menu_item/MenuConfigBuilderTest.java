package ru.itsyn.cuba.menu_editor.web.menu_item;

import org.dom4j.DocumentHelper;
import org.junit.jupiter.api.Test;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MenuConfigBuilderTest {

    @Test
    void testGetElementName() {
        var builder = new MenuConfigBuilder();
        assertEquals("menu", builder.getElementName(MenuItemType.MENU));
        assertEquals("separator", builder.getElementName(MenuItemType.SEPARATOR));
        assertEquals("item", builder.getElementName(MenuItemType.SCREEN));
        assertEquals("item", builder.getElementName(null));
    }

    @Test
    void testAddAttributeValue() {
        var builder = new MenuConfigBuilder();
        var e = DocumentHelper.createElement("e1");
        builder.addAttributeValue(e, "a1", null);
        assertNull(e.attribute("a1"));
        assertEquals(0, e.attributeCount());
        builder.addAttributeValue(e, "a1", "v1");
        assertEquals("v1", e.attribute("a1").getValue());
        builder.addAttributeValue(e, "a2", 2);
        assertEquals("2", e.attribute("a2").getValue());
        builder.addAttributeValue(e, "a3", MenuItemType.MENU);
        assertEquals("MENU", e.attribute("a3").getValue());
    }

}