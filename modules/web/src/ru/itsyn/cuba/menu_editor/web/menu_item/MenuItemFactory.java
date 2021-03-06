package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.config.MenuItem;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;
import ru.itsyn.cuba.menu_editor.entity.MenuOpenType;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.global.UuidProvider.createUuid;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

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
        re.setCaptionKey(messages.getMessage(MESSAGE_PACK, "rootItemCaption"));
        return re;
    }

    public MenuItemEntity createItem(MenuItem item) {
        var e = new MenuItemEntity();
        e.setId(item.getId());
        e.setCaptionKey(item.getCaption());
        e.setDescription(item.getDescription());
        e.setStyleName(item.getStylename());
        e.setIcon(item.getIcon());
        if (item.isMenu()) {
            e.setItemType(MenuItemType.MENU);
            e.setExpanded(item.isExpanded());
        } else if (item.isSeparator()) {
            e.setItemType(MenuItemType.SEPARATOR);
            e.setId(buildSeparatorId(item));
            e.setCaptionKey(messages.getMessage(e.getItemType()));
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
                e.setContentXml(buildContentXml(d));
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

    String buildContentXml(Element d) {
        if (d.content().isEmpty())
            return null;
        var xml = d.asXML();
        xml = substringAfter(xml, ">");
        xml = substringBeforeLast(xml, "<");
        return xml.lines().map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("\n"));
    }

    Boolean parseBoolean(String value) {
        if (value == null)
            return null;
        return Boolean.valueOf(value);
    }

}
