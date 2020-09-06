package com.kindof.catchthebeat.screens.beatmapeditor.settings;

public class Settings {
    private String samples, title, artist, tags, fruitType;
    private float offset, scrollSpeed, healthRate, difficulty, bpm;
    private Long beatmapId;

    /*
     * ScrollSpeed:
     *      ScrollSpeed = (0 ; +inf), but
     *      real-ScrollSpeed = ScrollSpeed * 100.0f
     *
     * HealthRate:
     *      HealthRate = (0 ; +inf)
     *
     * Offset:
     *      offset = (0 ; +inf)
     *
     * Difficulty:
     *      difficulty = (0 ; + inf)
     *
     * BPM:
     *      bpm = (0 ; +inf)
     *
     * Samples:
     *      samples = [normal, soft, ...]
     *
     * FruitType:
     *      fruitType = [apple, bananas, grapes, orange, pear]
     *
     * Title:
     *      title = any_string
     *
     * Artist:
     *      artist = any_string
     *
     * Tags:
     *      tags = "tag1 tag2 tag3 tag4 tagN"
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    public Settings() {
        fruitType = "apple";
        samples = title = artist = tags = "";
        offset = difficulty = bpm = 0;
        healthRate = 10;
        scrollSpeed = 900;
        beatmapId = 0L;
    }

    public Settings(Long beatmapId, String samples, String title, String artist, String tags, float offset, float scrollSpeed, float healthRate, float difficulty, float bpm) {
        this.beatmapId = beatmapId;
        this.samples = samples;
        this.title = title;
        this.artist = artist;
        this.tags = tags;
        this.offset = offset;
        this.scrollSpeed = scrollSpeed;
        this.healthRate = healthRate;
        this.difficulty = difficulty;
        this.bpm = bpm;
    }

    public void setFruitType(String fruitType) {
        this.fruitType = fruitType;
    }

    public void setSamples(String samples) {
        this.samples = samples;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public void setScrollSpeed(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public void setHealthRate(float healthRate) {
        this.healthRate = healthRate;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void setBpm(float bpm) {
        this.bpm = bpm;
    }

    public void setBeatmapId(Long beatmapId) {
        this.beatmapId = beatmapId;
    }

    public String getFruitType() {
        return fruitType;
    }

    public String getSamples() {
        return samples;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getTags() {
        return tags;
    }

    public float getOffset() {
        return offset;
    }

    public float getScrollSpeed() {
        return scrollSpeed;
    }

    public float getHealthRate() {
        return healthRate;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public float getBpm() {
        return bpm;
    }

    public Long getBeatmapId() {
        return beatmapId;
    }
}
