package com.kindof.catchthebeat.screens.beatmapeditor;

public class BeatmapEntity {
    private String title, artist;
    private Long id;

    public BeatmapEntity() {

    }

    public BeatmapEntity(Long id, String title, String artist) {
        setId(id);
        setTitle(title);
        setArtist(artist);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Long getId() {
        return id;
    }
}
