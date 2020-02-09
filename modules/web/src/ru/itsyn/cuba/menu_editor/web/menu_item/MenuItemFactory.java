package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.config.MenuItem;
import org.springframework.stereotype.Component;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;
import ru.itsyn.cuba.menu_editor.entity.MenuOpenType;

import javax.inject.Inject;

import static com.haulmont.cuba.core.global.UuidProvider.createUuid;
import static java.lang.Boolean.parseBoolean;

@Component("menu_MenuItemFactory")
public class MenuItemFactory {

    static String messagePack = MenuItemFactory.class.getPackageName();

    @Inject
    Messages messages;

    public MenuItemEntity createRootItem() {
        var re = new MenuItemEntity();
        re.setId("root");
        re.setItemType(MenuItemType.MENU);
        re.setCaption(messages.getMessage(messagePack, "rootItemCaption"));
        return re;
    }

    public MenuItemEntity createItem(MenuItem item) {
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
            e.setId(buildSeparatorId(item));
            e.setCaption(messages.getMessage(e.getItemType()));
        } else {
            e.setItemType(MenuItemType.SCREEN);
            e.setScreen(item.getScreen());
            e.setRunnableClass(item.getRunnableClass());
            e.setBean(item.getBean());
            e.setBeanMethod(item.getBeanMethod());
            var d = item.getDescriptor();
            if (d != null) {
                e.setOpenType(MenuOpenType.fromId(d.attributeValue("openType")));
                e.setResizable(parseBoolean(d.attributeValue("resizable")));
                e.setShortcut(d.attributeValue("shortcut"));
            }
        }
        return e;
    }

    String buildSeparatorId(MenuItem item) {
        var parent = item.getParent();
        if (parent == null)
            return createUuid().toString();
        return "separator-" + parent.getChildren().indexOf(item);
    }

}
