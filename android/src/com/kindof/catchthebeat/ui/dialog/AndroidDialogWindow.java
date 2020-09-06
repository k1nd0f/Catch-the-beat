package com.kindof.catchthebeat.ui.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.kindof.catchthebeat.AndroidLauncher;
import com.kindof.catchthebeat.R;
import com.kindof.catchthebeat.ui.actors.dialog.DialogButton;
import com.kindof.catchthebeat.ui.actors.dialog.IDialogWindow;

@SuppressLint("ValidFragment")
public class AndroidDialogWindow extends DialogFragment implements IDialogWindow {
    private static final String TAG = "Android Dialog Window";

    private AndroidLauncher androidLauncher;
    private AlertDialog.Builder alertDialogBuilder;
    private DialogButton positiveDialogButton, neutralDialogButton, negativeDialogButton;
    private String title, message;
    private boolean useIcon;

    public AndroidDialogWindow(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        alertDialogBuilder = new AlertDialog.Builder(androidLauncher);
        positiveDialogButton = negativeDialogButton = neutralDialogButton = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialogBuilder.setTitle(title)
                .setMessage(message);

        if (useIcon) {
            alertDialogBuilder.setIcon(R.drawable.ic_launcher);
        }
        if (positiveDialogButton != null) {
            alertDialogBuilder.setPositiveButton(positiveDialogButton.getName(), (dialogInterface, i) -> {
                Runnable runnable = positiveDialogButton.getOnClick();
                if (runnable != null) {
                    runnable.run();
                }
            });
        }
        if (neutralDialogButton != null) {
            alertDialogBuilder.setNeutralButton(neutralDialogButton.getName(), (dialogInterface, i) -> {
                Runnable runnable = neutralDialogButton.getOnClick();
                if (runnable != null) {
                    runnable.run();
                }
            });
        }
        if (negativeDialogButton != null) {
            alertDialogBuilder.setNegativeButton(negativeDialogButton.getName(), (dialogInterface, i) -> {
                Runnable runnable = negativeDialogButton.getOnClick();
                if (runnable != null) {
                    runnable.run();
                }
            });
        }

        return alertDialogBuilder.create();
    }

    @Override
    public void set(String title, String message, boolean useIcon, boolean isCancelable, DialogButton positiveDialogButton, DialogButton neutralDialogButton, DialogButton negativeDialogButton) {
        setCancelable(isCancelable);
        this.title = title;
        this.message = message;
        this.useIcon = useIcon;
        this.positiveDialogButton = positiveDialogButton;
        this.neutralDialogButton = neutralDialogButton;
        this.negativeDialogButton = negativeDialogButton;
    }

    @Override
    public DialogButton getPositiveDialogButton() {
        return positiveDialogButton;
    }

    @Override
    public DialogButton getNeutralDialogButton() {
        return neutralDialogButton;
    }

    @Override
    public DialogButton getNegativeDialogButton() {
        return negativeDialogButton;
    }

    @Override
    public void show() {
        FragmentManager fragmentManager = androidLauncher.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        show(fragmentTransaction, TAG);
    }
}
