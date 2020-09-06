package com.kindof.catchthebeat.ui.actors.dialog;

public class DialogButton {
    private String name;
    private Runnable onClick;

    public DialogButton(String name, Runnable onClick) {
        this.name = name;
        this.onClick = onClick;
    }

    public String getName() {
        return name;
    }

    public Runnable getOnClick() {
        return onClick;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }
}
