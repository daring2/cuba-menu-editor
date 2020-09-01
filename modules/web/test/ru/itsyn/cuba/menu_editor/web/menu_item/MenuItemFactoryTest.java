package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.config.MenuItem;
import org.dom4j.DocumentHelper;
import org.junit.Before;
import org.junit.Test;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;
import ru.itsyn.cuba.menu_editor.entity.MenuOpenType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemFactory.MESSAGE_PACK;
import static ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemFactory.ROOT_ITEM_ID;

public class MenuItemFactoryTest {

    MenuItemFactory factory;

    @Before
    public void init() {
        factory = new MenuItemFactory();
        factory.messages = mock(Messages.class);
    }

    @Test
    public void testCreateRootItem() {
        var f = factory;
        when(f.messages.getMessage(MESSAGE_PACK, "rootItemCaption"))
                .thenReturn("root caption");
        var ri = f.createRootItem();
        assertEquals(ROOT_ITEM_ID, ri.getId());
        assertEquals(MenuItemType.MENU, ri.getItemType());
        assertEquals("root caption", ri.getCaptionKey());
    }

    @Test
    public void testCreateItem() {
        var f = factory;
        when(f.messages.getMainMessage(anyString()))
                .then(i -> i.getArgument(0) + ".message");
        when(f.messages.getMessage(MenuItemType.SEPARATOR))
                .thenReturn("separator caption");

        var mi = new MenuItem("mi1");
        mi.setCaption("caption1");
        mi.setDescription("desc1");
        mi.setStylename("style1");
        mi.setIcon("icon1");
        mi.setExpanded(true);
        mi.setScreen("screen1");
        mi.setRunnableClass("runnableClass");
        mi.setBean("bean1");
        mi.setBeanMethod("beanMethod1");

        var de = DocumentHelper.createDocument().addElement("d");
        de.addAttribute("openType", "NEW_TAB");
        de.addAttribute("resizable", "true");
        de.addAttribute("shortcut", "shortcut1");
        mi.setDescriptor(de);

        mi.setMenu(true);
        mi.setSeparator(false);
        assertMenuItem(mi, MenuItemType.MENU);

        mi.setMenu(false);
        mi.setSeparator(false);
        assertMenuItem(mi, MenuItemType.SCREEN);

        mi.setMenu(false);
        mi.setSeparator(true);
        assertMenuItem(mi, MenuItemType.SEPARATOR);

        var ci = new MenuItem(mi, "ci1");
        ci.setMenu(false);
        ci.setSeparator(true);
        assertMenuItem(ci, MenuItemType.SEPARATOR);
    }

    void assertMenuItem(MenuItem mi, MenuItemType itemType) {
        var item = factory.createItem(mi);
        assertEquals(itemType, item.getItemType());
        if (itemType != MenuItemType.SEPARATOR) {
            assertEquals("caption1", item.getCaptionKey());
            assertEquals("desc1", item.getDescription());
            assertEquals("style1", item.getStyleName());
            assertEquals("icon1", item.getIcon());
        }
        if (itemType == MenuItemType.MENU) {
            assertEquals(true, item.getExpanded());
            assertNull(item.getScreen());
        } else if (itemType == MenuItemType.SEPARATOR) {
            assertEquals("separator caption", item.getCaptionKey());
            assertNull(item.getScreen());
            if (item.getParent() != null) {
                assertEquals("separator-0", item.getId());
            }
        } if (itemType == MenuItemType.SCREEN) {
            assertEquals("screen1", item.getScreen());
            assertEquals("runnableClass", item.getRunnableClass());
            assertEquals("bean1", item.getBean());
            assertEquals("beanMethod1", item.getBeanMethod());
            assertEquals(MenuOpenType.NEW_TAB, item.getOpenType());
            assertEquals(true, item.getResizable());
            assertEquals("shortcut1", item.getShortcut());
            assertNull(item.getExpanded());
        }

    }

}