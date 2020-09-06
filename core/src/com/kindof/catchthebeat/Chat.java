package com.kindof.catchthebeat;

import com.kindof.catchthebeat.users.Message;

import java.util.ArrayList;

public class Chat {
    private Integer id;
    private ArrayList<Message> chatMessages;

    public Chat() {

    }

    public Chat(Integer id) {
        setId(id);
        setChatMessages(new ArrayList<Message>());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChatMessages(ArrayList<Message> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public Integer getId() {
        return id;
    }

    public ArrayList<Message> getChatMessages() {
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
