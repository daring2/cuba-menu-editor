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
import static org.springframework.util.StringUtils.isEmpty;

@Component("menu_MenuItemFactory")
public class MenuItemFactory {

    public static final String ROOT_ITEM_ID = "rootItem";
    public static final String MESSAGE_PACK = MenuItemFactory.class.getPackageName();

    @Inject
    Messages messages;

    public MenuItemEntity createRootItem() {
        var re = new MenuItemEntity();
        re.setId(ROOT_ITEM_ID);
        re.setItemType(MenuItemType.MENU);
        re.setCaption(messages.getMessage(MESSAGE_PACK, "rootItemCaption"));
        return re;
    }

    public MenuItemEntity createItem(MenuItem item) {
        var e = new MenuItemEntity();
        e.setId(item.getId());
        e.setCaption(getCaption(item));
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

    String getCaption(MenuItem item) {
        var caption = item.getCaption();
        if (isEmpty(caption)) {
            var key = "menu-config." + item.getId();
            caption = messages.getMainMessage(key);
        }
        return caption;
    }

    String buildSeparatorId(MenuItem item) {
        var parent = item.getParent();
        if (parent == null)
            return createUuid().toString();
        return "separator-" + parent.getChildren().indexOf(item);
    }

}
