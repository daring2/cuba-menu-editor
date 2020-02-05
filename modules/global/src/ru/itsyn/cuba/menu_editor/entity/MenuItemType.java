package ru.itsyn.cuba.menu_editor.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum MenuItemType implements EnumClass<String> {

    MENU("MENU"),
    SCREEN("SCREEN"),
    SEPARATOR("SEPARATOR");

    private String id;

    MenuItemType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static MenuItemType fromId(String id) {
        for (MenuItemType at : MenuItemType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

}