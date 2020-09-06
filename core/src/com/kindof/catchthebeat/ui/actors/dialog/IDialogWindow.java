package com.kindof.catchthebeat.ui.actors.dialog;

public interface IDialogWindow {

    void show();

    void set(String title, String message, boolean useIcon, boolean isCancelable, DialogButton positiveDialogButton, DialogButton neutralDialogButton, DialogButton negativeDialogButton);

    DialogButton getPositiveDialogButton();

    DialogButton getNeutralDialogButton();

    DialogButton getNegativeDialogButton();

    boolean isHidden();

    boolean isAdded();

    boolean isVisible();

    boolean isRemoving();

    void dismiss();
}
