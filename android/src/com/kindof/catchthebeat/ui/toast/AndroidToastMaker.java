package com.kindof.catchthebeat.ui.toast;

import android.widget.Toast;

import com.kindof.catchthebeat.AndroidLauncher;
import com.kindof.catchthebeat.ui.toastmaker.IToastMaker;

public class AndroidToastMaker implements IToastMaker {
    private AndroidLauncher androidLauncher;

    public AndroidToastMaker(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public void makeToast(String text, int length) {
        androidLauncher.getHandler().post(() -> Toast.makeText(androidLauncher, text, length).show());
    }
}
