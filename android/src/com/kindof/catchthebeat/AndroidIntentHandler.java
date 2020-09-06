package com.kindof.catchthebeat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class AndroidIntentHandler implements IIntentHandler {
    private AndroidApplication androidApplication;
    private Context context;
    private Uri selectedFileUri;
    public static final int SELECT_CODE = 1;

    public AndroidIntentHandler(AndroidApplication androidApplication) {
        this.androidApplication = androidApplication;
        this.context = androidApplication.getApplicationContext();
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
        androidApplication.startActivityForResult(Intent.createChooser(intent, title), SELECT_CODE);
    }

    public void setSelectedFileUri(Uri selectedFileUri) {
        this.selectedFileUri = selectedFileUri;
    }

    @Override
    public Object getSelectedFileUri() {
        return selectedFileUri;
    }

    @Override
    public void triggerRebirth() {
        ProcessPhoenix.triggerRebirth(context, new Intent(context, AndroidLauncher.class));
    }
}
