package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.cuba.core.global.Messages;
import org.junit.Before;
import org.junit.Test;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemFactory.messagePack;

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
        when(f.messages.getMessage(messagePack, "rootItemCaption"))
                .thenReturn("root caption");
        var ri = f.createRootItem();
        assertEquals("root", ri.getId());
        assertEquals(MenuItemType.MENU, ri.getItemType());
        assertEquals("root caption", ri.getCaption());
    }

    @Test
    public void testCreateItem() {
        //TODO implement
    }

}