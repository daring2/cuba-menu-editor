package ru.itsyn.cuba.menu_editor.web.menu_entity;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.MenuItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;
import ru.itsyn.cuba.menu_editor.entity.MenuOpenType;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("menu_MenuItemLoader")
@Scope(SCOPE_PROTOTYPE)
@NotThreadSafe
public class MenuItemLoader {

    @Inject
    ApplicationContext appContext;
    @Inject
    Messages messages;

    ConfigLoader configLoader = new ConfigLoader();
    int itemIds = 1;

    @PostConstruct
    public void init() {
        appContext.getAutowireCapableBeanFactory().autowireBean(configLoader);
    }

    public MenuItemEntity loadMenu(MenuEntity menu) {
        var rootItem = createRootItem();
        var items = configLoader.loadConfig(menu.getConfig());
        items.forEach(i -> buildEntities(i, rootItem));
        return rootItem;
    }

    MenuItemEntity createRootItem() {
        var re = new MenuItemEntity();
        re.setId("root");
        re.setItemType(MenuItemType.MENU);
        re.setCaption("Main menu");
        return re;
    }

    void buildEntities(MenuItem item, MenuItemEntity parent) {
        var e = buildEntity(item);
        e.setParent(parent);
        parent.getChildren().add(e);
        item.getChildren().forEach(i -> buildEntities(i, e));
    }

    MenuItemEntity buildEntity(MenuItem item) {
        var e = new MenuItemEntity();
        e.setId(item.getId());
        e.setCaption(messages.getMainMessage("menu-config." + e.getId()));
        e.setDescription(item.getDescription());
        e.setStyleName(item.getStylename());
        e.setIcon(item.getIcon());
        if (item.isMenu()) {
            e.setItemType(MenuItemType.MENU);
            e.setExpanded(item.isExpanded());
        } else if (item.isSeparator()) {
            e.setItemType(MenuItemType.SEPARATOR);
            e.setId("separator" + itemIds++);
            e.setCaption(messages.getMessage(e.getItemType()));
        } else {
            e.setItemType(MenuItemType.SCREEN);
            e.setScreen(item.getScreen());
            e.setRunnableClass(item.getRunnableClass());
            e.setBean(item.getBean());
            e.setBeanMethod(item.getBeanMethod());
        }
        var d = item.getDescriptor();
        if (d != null) {
            e.setOpenType(MenuOpenType.fromId(d.attributeValue("openType")));
            e.setResizable(parseBoolean(d.attributeValue("resizable")));
            e.setShortcut(d.attributeValue("shortcut"));
        }
        return e;
    }

    static class ConfigLoader extends MenuConfig {

        public List<MenuItem> loadConfig(String xml) {
            rootItems = new ArrayList<>();
            var re = Dom4j.readDocument(xml).getRootElement();
            loadMenuItems(re, null);
            return rootItems;
        }

    }

}
