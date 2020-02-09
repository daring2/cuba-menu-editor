package ru.itsyn.cuba.menu_editor.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseStringIdEntity;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NamePattern("%s|caption")
@MetaClass(name = "menu_MenuItemEntity")
public class MenuItemEntity extends BaseStringIdEntity {
    private static final long serialVersionUID = -6335439802277376563L;

    @NotNull
    @MetaProperty(mandatory = true)
    protected String id;

    @MetaProperty
    protected String caption;

    @NotNull
    @MetaProperty(mandatory = true)
    protected String itemType = MenuItemType.SCREEN.getId();

    @MetaProperty
    protected MenuItemEntity parent;

    @MetaProperty
    protected String screen;

    @MetaProperty
    protected String runnableClass;

    @MetaProperty
    protected String bean;

    @MetaProperty
    protected String beanMethod;

    @MetaProperty
    protected String openType;

    @MetaProperty
    protected Boolean resizable;

    @MetaProperty
    protected Boolean expanded;

    @MetaProperty
    protected String shortcut;

    @MetaProperty
    protected String description;

    @MetaProperty
    protected String styleName;

    @MetaProperty
    protected String icon;

    @MetaProperty
    protected List<MenuItemEntity> children = new ArrayList<>();

    //TODO add attributes params, permissions, screenProperties

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<MenuItemEntity> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItemEntity> children) {
        this.children = children;
    }

    public MenuItemEntity getParent() {
        return parent;
    }

    public void setParent(MenuItemEntity parent) {
        this.parent = parent;
    }

    public MenuItemType getItemType() {
        return itemType == null ? null : MenuItemType.fromId(itemType);
    }

    public void setItemType(MenuItemType itemType) {
        this.itemType = itemType == null ? null : itemType.getId();
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public MenuOpenType getOpenType() {
        return openType == null ? null : MenuOpenType.fromId(openType);
    }

    public void setOpenType(MenuOpenType openType) {
        this.openType = openType == null ? null : openType.getId();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public Boolean getResizable() {
        return resizable;
    }

    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

    public String getBeanMethod() {
        return beanMethod;
    }

    public void setBeanMethod(String beanMethod) {
        this.beanMethod = beanMethod;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getRunnableClass() {
        return runnableClass;
    }

    public void setRunnableClass(String runnableClass) {
        this.runnableClass = runnableClass;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void visitItems(Consumer<MenuItemEntity> consumer) {
        consumer.accept(this);
        children.forEach(i -> i.visitItems(consumer));
    }

}