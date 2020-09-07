package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;
import ru.itsyn.cuba.menu_editor.entity.MenuItemType;
import ru.itsyn.cuba.menu_editor.entity.MenuOpenType;

import javax.inject.Inject;

import static java.util.Arrays.asList;

@UiController("menu_MenuItemEntity.edit")
@UiDescriptor("menu-item-edit.xml")
@EditedEntityContainer("editDc")
@LoadDataBeforeShow
public class MenuItemEditor extends StandardEditor<MenuItemEntity> {

    @Inject
    protected LookupField<MenuItemType> itemTypeField;
    @Inject
    protected LookupField<MenuOpenType> openTypeField;
    @Inject
    protected LookupField<Boolean> resizableField;
    @Inject
    protected CheckBox expandedField;

    @Subscribe
    public void onInit(InitEvent event) {
        resizableField.setOptionsList(asList(Boolean.TRUE, Boolean.FALSE));
        itemTypeField.addValueChangeListener(e -> updateFields());
        openTypeField.addValueChangeListener(e -> updateFields());
    }

    protected void updateFields() {
        var itemType= itemTypeField.getValue();
        var openType = openTypeField.getValue();
        resizableField.setEditable(itemType == MenuItemType.SCREEN && openType == MenuOpenType.DIALOG);
        expandedField.setEditable(itemType == MenuItemType.MENU);
    }

}