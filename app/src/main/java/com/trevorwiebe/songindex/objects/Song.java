package com.trevorwiebe.songindex.objects;

/**
 * Created by thisi on 10/25/2018.
 */

public class Song {

    private String name;
    private String poet;
    private String composer;
    private String url;
    private boolean isCopyRighted;
    private long dateAdded;
    private String songId;

    public Song(String name, String poet, String composer, String url, boolean isCopyRighted, long dateAdded, String songId) {
        this.name = name;
        this.poet = poet;
        this.composer = composer;
        this.url = url;
        this.isCopyRighted = isCopyRighted;
        this.dateAdded = dateAdded;
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoet() {
        return poet;
    }

    public void setPoet(String poet) {
        this.poet = poet;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCopyRighted() {
        return isCopyRighted;
    }

    public void setCopyRighted(boolean copyRighted) {
        isCopyRighted = copyRighted;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
