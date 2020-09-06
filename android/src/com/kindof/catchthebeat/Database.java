package com.kindof.catchthebeat;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEntity;
import com.kindof.catchthebeat.tools.Time;
import com.kindof.catchthebeat.users.Friend;
import com.kindof.catchthebeat.users.Message;
import com.kindof.catchthebeat.users.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Database implements IDatabase {
    private FirebaseDatabase database;
    private DatabaseReference usersRef, beatmapsRef, beatmapCountRef, chatsRef, messageCountRef, chatCountRef;
    private HashMap<String, User> users;                                // HashMap<User-UID, User>
    private HashMap<Long, BeatmapEntity> beatmaps;                      // HashMap<Beatmap-ID, BeatmapEntity>
    private Long beatmapCount, messageCount;
    private Integer chatCount;
    private HashMap<String, Chat> chats;                                // HashMap<Chat-ID, Chat>

    public Database() {
        users = new HashMap<>();
        beatmaps = new HashMap<>();
        chats = new HashMap<>();
        beatmapCount = messageCount = 0L;
        chatCount = 0;
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        beatmapsRef = database.getReference("Beatmaps");
        beatmapCountRef = database.getReference("beatmapCount");
        chatsRef = database.getReference("Chats");
        messageCountRef = database.getReference("messageCount");
        chatCountRef = database.getReference("chatCount");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    users.put(user.getUid(), user);
                    if (user.getFriends() == null) {
                        user.setFriends(new ArrayList<Friend>());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        beatmapsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    BeatmapEntity beatmapEntity = data.getValue(BeatmapEntity.class);
                    beatmaps.put(beatmapEntity.getId(), beatmapEntity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        beatmapCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                beatmapCount = (Long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final Chat chat = data.getValue(Chat.class);
                    String chatId = chat.getId().toString();
                    chats.put(chatId, chat);
                    if (Res.CURRENT_CHAT_SCREEN != null && Res.CURRENT_CHAT_SCREEN.getChat().getId().equals(chat.getId())) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Message> chatMessages = chat.getChatMessages();
                                Res.CURRENT_CHAT_SCREEN.reInitMessageItems(chatMessages);
                                if (Res.USER.getUid().equals(chatMessages.get(chatMessages.size() - 1).getSenderUid()))
                                    Res.CURRENT_CHAT_SCREEN.scrollToLastMessage();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        messageCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageCount = (Long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        chatCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatCount = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setUser(User user) {
        usersRef.child(user.getUid()).setValue(user);
    }

    @Override
    public User getUser(String uid) {
        return users.get(uid);
    }

    @Override
    public HashMap<String, User> getUsers() {
        return users;
    }

    @Override
    public void setBeatmap(BeatmapEntity beatmap) {
        beatmapsRef.child(beatmapCount.toString()).setValue(beatmap);
        setBeatmapCount(beatmapCount + 1);
    }

    @Override
    public BeatmapEntity getBeatmap(Long id) {
        return beatmaps.get(id);
    }

    @Override
    public HashMap<Long, BeatmapEntity> getBeatmaps() {
        return beatmaps;
    }

    @Override
    public void setBeatmapCount(Long beatmapCount) {
        beatmapCountRef.setValue(beatmapCount);
    }

    @Override
    public Long getBeatmapCount() {
        return beatmapCount;
    }

    @Override
    public void setMessageCount(Long messageCount) {
        messageCountRef.setValue(messageCount);
    }

    @Override
    public Long getMessageCount() {
        return messageCount;
    }

    @Override
    public void setChatCount(Integer chatCount) {
        chatCountRef.setValue(chatCount);
    }

    @Override
    public Integer getChatCount() {
        return chatCount;
    }

    @Override
    public void addMessage(Integer chatId, Message message) {
        Chat chat = chats.get(chatId.toString());
        chat.addMessage(message);
        chatsRef.setValue(chats);
        setMessageCount(messageCount + 1);
    }

    @Override
    public void addChat(Chat chat) {
        chats.put(chat.getId().toString(), chat);
        setChatCount(chatCount + 1);
    }

    @Override
    public Chat getChat(Integer chatId) {
        return chats.get(chatId.toString());
    }

    @Override
    public HashMap<String, Chat> getChats() {
        return chats;
    }

    @Override
    public Chat createNewChat() {
        Chat chat = new Chat(chatCount);
        addChat(chat);
        return chat;
    }

    @Override
    public void removeChat(Long chatId) {
        chats.remove(chatId.toString());
    }

    @Override
    public void putChat(Chat chat) {
        chats.put(chat.getId().toString(), chat);
    }

    @Override
    public Message createNewMessage(String senderUid, String text) {
        return new Message(senderUid, messageCount, Time.getStringOfTime(), text);
    }
}
