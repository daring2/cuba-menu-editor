package ru.itsyn.cuba.menu_editor.entity;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class MenuItemEntityTest {

    @Test
    public void testVisitItems() {
        var ri = newItem("root", null);
        var i1 = newItem("i1", ri);
        var i2 = newItem("i2", ri);
        var i21 = newItem("i21", i2);
        var items = new ArrayList<MenuItemEntity>();
        ri.visitItems(items::add);
        assertEquals(newArrayList(ri, i1, i2, i21), items);
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