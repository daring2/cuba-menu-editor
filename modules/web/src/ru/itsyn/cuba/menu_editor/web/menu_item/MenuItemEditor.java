package ru.itsyn.cuba.menu_editor.web.menu_item;

import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuItemEntity;

import javax.inject.Inject;

import static java.util.Arrays.asList;

@UiController("menu_MenuItemEntity.edit")
@UiDescriptor("menu-item-edit.xml")
@EditedEntityContainer("editDc")
@LoadDataBeforeShow
public class MenuItemEditor extends StandardEditor<MenuItemEntity> {

    @Inject
    protected LookupField<Boolean> resizableField;

    @Subscribe
    public void onInit(InitEvent event) {
        resizableField.setOptionsList(asList(Boolean.TRUE, Boolean.FALSE));
    }

}