package ru.itsyn.cuba.menu_editor.web.util;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs.OptionDialogBuilder;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.web.AppUI;

import java.util.function.Consumer;

import static com.haulmont.cuba.gui.Dialogs.MessageType.CONFIRMATION;

public class DialogUtils {

    public static OptionDialogBuilder newConfirmationDialog(
            String message,
            Consumer<ActionPerformedEvent> action
    ) {
        var messages = AppBeans.get(Messages.class);
        var caption = messages.getMainMessage("dialogs.Confirmation");
        var actions = new BaseAction[] {
                new DialogAction(DialogAction.Type.YES).withHandler(action),
                new DialogAction(DialogAction.Type.CANCEL)
        };
        var dialogs = AppUI.getCurrent().getDialogs();
        return dialogs.createOptionDialog(CONFIRMATION)
                .withCaption(caption)
                .withMessage(message)
                .withActions(actions);
    }

    private DialogUtils() {
    }

}
