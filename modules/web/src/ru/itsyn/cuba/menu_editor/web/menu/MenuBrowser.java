package ru.itsyn.cuba.menu_editor.web.menu;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import ru.itsyn.cuba.menu_editor.entity.MenuEntity;

import javax.inject.Inject;

@UiController("menu_MenuEntity.browse")
@UiDescriptor("menu-browse.xml")
@LookupComponent("table")
@LoadDataBeforeShow
public class MenuBrowser extends StandardLookup<MenuEntity> {

    @Inject
    protected Metadata metadata;
    @Inject
    protected ScreenBuilders screenBuilders;
    @Inject
    protected AppMenuManager appMenuManager;
    @Inject
    protected Table<MenuEntity> table;

    @Subscribe("table.apply")
    public void onMenuApply(ActionPerformedEvent event) {
        appMenuManager.reloadAppMenu();
    }

    @Subscribe("table.copy")
    public void onMenuCopy(ActionPerformedEvent event) {
        var entity = table.getSingleSelected();
        if (entity == null)
            return;
        var ce = metadata.getTools().copy(entity);
        ce.setId(UuidProvider.createUuid());
        ce.setName(ce.getName() + " - Copy");
        screenBuilders.editor(table)
                .newEntity(ce)
                .show();
    }

}