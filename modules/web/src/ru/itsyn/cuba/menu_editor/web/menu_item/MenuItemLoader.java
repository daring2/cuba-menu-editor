package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.MenuItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("menu_MenuItemLoader")
@Scope(SCOPE_PROTOTYPE)
@NotThreadSafe
public class MenuItemLoader {

    @Inject
    ApplicationContext appContext;
    @Inject
    MenuItemFactory menuItemFactory;

    ConfigLoader configLoader = new ConfigLoader();
    int itemIds = 1;

    @PostConstruct
    public void init() {
        appContext.getAutowireCapableBeanFactory().autowireBean(configLoader);
    }

    public MenuItemEntity loadMenu(MenuEntity menu) {
        var rootItem = menuItemFactory.createRootItem();
        var items = configLoader.loadConfig(menu.getConfig());
        items.forEach(i -> buildEntities(i, rootItem));
        return rootItem;
    }

    void buildEntities(MenuItem item, MenuItemEntity parent) {
        var e = menuItemFactory.createItem(item);
        e.setParent(parent);
        parent.getChildren().add(e);
        item.getChildren().forEach(i -> buildEntities(i, e));
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
