package com.kindof.catchthebeat.users;

public class Message implements Comparable<Message> {
    private String senderUid, time, text;
    private Long id;

    public Message() {

    }

    public Message(String senderUid, Long id, String time, String text) {
        setSenderUid(senderUid);
        setId(id);
        setText(text);
        setTime(time);
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    @Override
    public boolean equals(Object message) {
        return this.id.equals(((Message) message).getId());
    }

    @Override
    public int compareTo(Message message) {
        return message.getId().compareTo(this.id);
    }
}
