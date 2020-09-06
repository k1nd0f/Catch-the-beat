package com.kindof.catchthebeat.users;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Res;

import java.util.ArrayList;
import java.util.Iterator;

public class User implements Comparable {
    private String nickname;
    private String uid;
    private int  totalScore, playCount, completedBeatmapCount;
    private float avgAccuracy, avgDifficulty, totalAccuracy, totalDifficulty;
    private ArrayList<Friend> friends;

    public User() {

    }

    public User(String uid, String nickname, int totalScore, int playCount, int completedBeatmapCount, float avgAccuracy, float avgDifficulty, int totalAccuracy, int totalDifficulty) {
        this.uid = uid;
        this.nickname = nickname;
        this.totalScore = totalScore;
        this.playCount = playCount;
        this.completedBeatmapCount = completedBeatmapCount;
        this.avgAccuracy = avgAccuracy;
        this.totalAccuracy = totalAccuracy;
        this.avgDifficulty = avgDifficulty;
        this.totalDifficulty = totalDifficulty;
        friends = new ArrayList<>();
    }

    public void updateData() {
        avgAccuracy = totalAccuracy / completedBeatmapCount;
        avgDifficulty = totalDifficulty / completedBeatmapCount;
    }

    public void incTotalScore(int score) {
        totalScore += score;
    }

    public void incPlayCount() {
        playCount++;
    }

    public void incCompletedBeatmapCount() {
        completedBeatmapCount++;
    }

    public void incTotalAccuracy(float accuracy) {
        totalAccuracy += accuracy;
    }

    public void incTotalDifficulty(float difficulty) {
        totalDifficulty += difficulty;
    }

    public void initIcon() {
        Texture texture = new Texture(Gdx.files.local(Res.LOCAL_PATH_TO_USERS_DIRECTORY + uid + "/icon"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar().initCurrentUserIcon(new Image(texture));
        Res.STATISTIC_SCREEN.initUserIcon(new Image(texture));
    }

    public void initDefaultIcon() {
        Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar().initCurrentUserIcon(new Image(Res.SKIN_ATLAS.findRegion(Res.USER_DEFAULT_ICON)));
        Res.STATISTIC_SCREEN.initUserIcon(new Image(Res.SKIN_ATLAS.findRegion(Res.USER_DEFAULT_ICON)));
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void setCompletedBeatmapCount(int completedBeatmapCount) {
        this.completedBeatmapCount = completedBeatmapCount;
    }

    public void setAvgAccuracy(float avgAccuracy) {
        this.avgAccuracy = avgAccuracy;
    }

    public void setAvgDifficulty(float avgDifficulty) {
        this.avgDifficulty = avgDifficulty;
    }

    public void setTotalAccuracy(float totalAccuracy) {
        this.totalAccuracy = totalAccuracy;
    }

    public void setTotalDifficulty(float totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getPlayCount() {
        return playCount;
    }

    public int getCompletedBeatmapCount() {
        return completedBeatmapCount;
    }

    public float getAvgAccuracy() {
        return avgAccuracy;
    }

    public float getTotalAccuracy() {
        return totalAccuracy;
    }

    public float getAvgDifficulty() {
        return avgDifficulty;
    }

    public float getTotalDifficulty() {
        return totalDifficulty;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void addToFriendsList(String uid) {
        IDatabase database = Res.GAME.getDatabase();
        Integer chatId = database.createNewChat().getId();
        if (!userInFriendList(uid))
            friends.add(new Friend(uid, chatId));
    }

    public void deleteFromFriendsList(String uid) {
        Iterator<Friend> iterator = friends.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getUid().equals(uid)) {
                iterator.remove();
                break;
            }
        }
    }

    public boolean userInFriendList(String uid) {
        for (Friend friend1 : friends) {
            if (friend1.getUid().equals(uid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User {\n\tstring::nickname -> " + nickname + "\n\tstring::uid -> " + uid + "\n}";
    }

    @Override
    public int compareTo(Object object) {
        User user = (User) object;
        return nickname.compareTo(user.nickname);
    }

    public Integer getChatId(String friendUid) {
        for (Friend friend : friends) {
            if (friend.getUid().equals(friendUid)) {
                return friend.getChatId();
            }
        }

        return null;
    }
}
