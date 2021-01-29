package ru.itsyn.cuba.menu_editor.web.menu_item;

import org.junit.jupiter.api.Test;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.itsyn.cuba.menu_editor.web.menu_item.MenuItemUtils.buildItemList;

class MenuItemUtilsTest {

    @Test
    public void testBuildItemList() {
        var ri = newItem("root", null);
        var i1 = newItem("i1", ri);
        var i2 = newItem("i2", ri);
        assertEquals(newArrayList(ri, i1, i2), buildItemList(ri));
    }

    MenuItemEntity newItem(String id, MenuItemEntity parent) {
        var i = new MenuItemEntity();
        i.setId(id);
        if (parent != null) {
            i.setParent(parent);
            parent.getChildren().add(i);
        }
        return i;
    }

}