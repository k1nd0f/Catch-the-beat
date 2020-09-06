package com.kindof.catchthebeat.user;

public class Friend {
    private String uid;
    private Long chatId;

    public Friend() {

    }

    public Friend(String uid, Long chatId) {
        setUid(uid);
        setChatId(chatId);
    }

    public String getUid() {
        return uid;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
