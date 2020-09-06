package com.kindof.catchthebeat.user.chat;

import java.util.ArrayList;

public class Chat {
    private Long id;
    private ArrayList<com.kindof.catchthebeat.user.chat.Message> chatMessages;

    public Chat() {

    }

    public Chat(Long id) {
        setId(id);
        setChatMessages(new ArrayList<>());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatMessages(ArrayList<com.kindof.catchthebeat.user.chat.Message> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public Long getId() {
        return id;
    }

    public ArrayList<com.kindof.catchthebeat.user.chat.Message> getChatMessages() {
        return chatMessages;
    }

    public void addMessage(Message message) {
        chatMessages.add(message);
    }

    @Override
    public String toString() {
        return id + " / " + chatMessages;
    }
}
