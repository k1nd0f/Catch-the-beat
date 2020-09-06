package com.kindof.catchthebeat.storage;

public interface StorageListener {
    void onSuccessfulPutFile();

    void onCanceledPutFile();

    void onProgressPutFile(Object taskSnapshot);

    void onFailurePutFile(Exception e);

    void onSuccessfulGetFile();

    void onCanceledGetFile();

    void onProgressGetFile(Object taskSnapshot);

    void onFailureGetFile(Exception e);
}
