package com.kindof.catchthebeat.ui.intenthandler;

public interface IIntentHandler {
    void getGalleryImagePath();

    void getAudioPath();

    void getOszFilePath();

    Object getSelectedFileUri();

    void triggerRebirth();

    void launchAnotherApp(String path);
}
