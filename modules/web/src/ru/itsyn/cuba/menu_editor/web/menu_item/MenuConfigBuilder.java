package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.dom4j.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;

import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("menu_MenuConfigBuilder")
@Scope(SCOPE_PROTOTYPE)
@NotThreadSafe
public class MenuConfigBuilder {

    static final String MENU_NAMESPACE = "http://schemas.haulmont.com/cuba/menu.xsd";

    public Document buildMenuConfig(List<MenuItemEntity> items) {
        var doc = DocumentHelper.createDocument();
        var rootElement = doc.addElement("menu-config", MENU_NAMESPACE);
        items.forEach(i -> addMenuItem(rootElement, i));
        return doc;
    }

    void addMenuItem(Element parent, MenuItemEntity item) {
        var itemType = item.getItemType();
        var e = parent.addElement(getElementName(itemType));
        if (itemType == MenuItemType.SEPARATOR)
            return;
        addAttributeValue(e, "id", item.getId());
        addAttributeValue(e, "caption", item.getCaptionKey());
        addAttributeValue(e, "description", item.getDescription());
        addAttributeValue(e, "stylename", item.getStyleName());
        addAttributeValue(e, "icon", item.getIcon());
        if (itemType == MenuItemType.MENU) {
            if (isTrue(item.getExpanded()))
                addAttributeValue(e, "expanded", item.getExpanded());
            item.getChildren().forEach(i -> addMenuItem(e, i));
        } else if (itemType == MenuItemType.SCREEN){
            if (!item.getId().equals(item.getScreen()))
                addAttributeValue(e, "screen", item.getScreen());
            addAttributeValue(e, "class", item.getRunnableClass());
            addAttributeValue(e, "bean", item.getBean());
            addAttributeValue(e, "beanMethod", item.getBeanMethod());
            addAttributeValue(e, "openType", item.getOpenType());
            if (item.getResizable() != null)
                addAttributeValue(e, "resizable", item.getResizable());
            addAttributeValue(e, "shortcut", item.getShortcut());
            addContentXml(e, item.getContentXml());
        }
    }

    String getElementName(MenuItemType itemType) {
        if (itemType == MenuItemType.MENU) {
            return "menu";
        } else if (itemType == MenuItemType.SEPARATOR) {
            return "separator";
        } else {
            return "item";
        }
    }

    void addAttributeValue(Element e, String name, Object value) {
        if (value == null) return;
        if (value instanceof EnumClass<?>)
            value = ((EnumClass<?>) value).getId();
        e.addAttribute(name, value.toString());
    }

    void addContentXml(Element e, String xml) {
        try {
            if (isBlank(xml)) return;
            var docXml = String.format(
                    "<root xmlns=\"%s\">%s</root>",
                    MENU_NAMESPACE, xml
            );
            var doc = DocumentHelper.parseText(docXml);
            for (Node n : doc.getRootElement().content()) {
                n.setParent(null);
                e.add(n);
            }
        } catch (DocumentException ex) {
            throw new RuntimeException("Cannot parse contentXml", ex);
        }
    }

}
