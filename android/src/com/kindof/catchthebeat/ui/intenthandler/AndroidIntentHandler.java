package com.kindof.catchthebeat.ui.intenthandler;

import android.content.Intent;
import android.net.Uri;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.kindof.catchthebeat.AndroidLauncher;

public class AndroidIntentHandler implements IIntentHandler {
    public static final int SELECT_CODE = 1;

    private AndroidLauncher androidLauncher;
    private Uri selectedFileUri;

    public AndroidIntentHandler(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public void getGalleryImagePath() {
        getFilePath("image/*", "Select Image Path");
    }

    @Override
    public void getAudioPath() {
        getFilePath("audio/mpeg", "Select *.mp3 File");
    }

    @Override
    public void getOszFilePath() {
        getFilePath("application/*", "Select *.osz File");
    }

    private void getFilePath(String mime, String title) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType(mime);
        androidLauncher.startActivityForResult(Intent.createChooser(intent, title), SELECT_CODE);
    }

    public void setSelectedFileUri(Uri selectedFileUri) {
        this.selectedFileUri = selectedFileUri;
    }

    @Override
    public Object getSelectedFileUri() {
        return selectedFileUri;
    }

    @Override
    public void launchAnotherApp(String packageName) {
        Intent intent = androidLauncher.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            androidLauncher.startActivity(intent);
        }
    }

    @Override
    public void triggerRebirth() {
        ProcessPhoenix.triggerRebirth(androidLauncher, new Intent(androidLauncher, AndroidLauncher.class));
    }

    public enum ActivityPackage {
        gmail("com.google.android.gm"),
        yandex("ru.yandex.mail"),
        mail("ru.mail.mailapp");

        private String packageName;

        ActivityPackage(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageName() {
            return packageName;
        }
    }
}
