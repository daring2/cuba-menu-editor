package ru.itsyn.cuba.menu_editor.web.menu_item;

import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

import java.util.ArrayList;
import java.util.List;

public class MenuItemUtils {

    public static List<MenuItemEntity> buildItemList(MenuItemEntity rootItem) {
        var items = new ArrayList<MenuItemEntity>();
        rootItem.visitItems(items::add);
        return items;
    }

    private MenuItemUtils() {
    }

}
