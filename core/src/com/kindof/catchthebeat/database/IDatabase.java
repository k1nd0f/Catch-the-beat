package com.kindof.catchthebeat.database;

import com.kindof.catchthebeat.user.chat.Chat;
import com.kindof.catchthebeat.beatmaps.BeatmapEntity;
import com.kindof.catchthebeat.user.chat.Message;
import com.kindof.catchthebeat.user.User;

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

    void setChatCount(Long chatCount);

    Long getChatCount();

    void addMessage(Long chatId, Message message);

    void addChat(Chat chat);

    Chat getChat(Long chatId);

    HashMap<String, Chat> getChats();

    Chat createNewChat();

    void removeChat(Long chatId);

    void putChat(Chat chat);

    Message createNewMessage(String senderUid, String text);
}