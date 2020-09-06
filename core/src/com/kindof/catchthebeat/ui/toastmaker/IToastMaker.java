package com.kindof.catchthebeat.ui.toastmaker;

public interface IToastMaker {
    int LENGTH_SHORT = 0;
    int LENGTH_LONG = 1;

    void makeToast(String text, int length);
}
