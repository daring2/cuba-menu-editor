package ru.itsyn.cuba.menu_editor.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum MenuOpenType implements EnumClass<String> {

    NEW_TAB("NEW_TAB"),
    DIALOG("DIALOG"),
    NEW_WINDOW("NEW_WINDOW");

    private String id;

    MenuOpenType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static MenuOpenType fromId(String id) {
        for (MenuOpenType at : MenuOpenType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

}