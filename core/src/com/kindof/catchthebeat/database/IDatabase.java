package com.kindof.catchthebeat.database;

import com.kindof.catchthebeat.Chat;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEntity;
import com.kindof.catchthebeat.users.Message;
import com.kindof.catchthebeat.users.User;

import java.util.HashMap;

public interface IDatabase {

    void setUser(User user);

    User getUser(String uid);

    HashMap<String, User> getUsers();

    void setBeatmap(BeatmapEntity beatmap);

    BeatmapEntity getBeatmap(Long id);

    HashMap<Long, BeatmapEntity> getBeatmaps();

    void setBeatmapCount(Long id);

    Long getBeatmapCount();

    void setMessageCount(Long messageCount);

    Long getMessageCount();

    void setChatCount(Integer chatCount);

    Integer getChatCount();

    void addMessage(Integer chatId, Message message);

    void addChat(Chat chat);

    Chat getChat(Integer chatId);

    HashMap<String, Chat> getChats();

    Chat createNewChat();

    void removeChat(Long chatId);

    void putChat(Chat chat);

    Message createNewMessage(String senderUid, String text);
}