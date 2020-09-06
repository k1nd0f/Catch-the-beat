package com.kindof.catchthebeat.users;

public class Friend {
    private String uid;
    private Integer chatId;

    public Friend() {

    }

    public Friend(String uid, Integer chatId) {
        setUid(uid);
        setChatId(chatId);
    }

    public String getUid() {
        return uid;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
