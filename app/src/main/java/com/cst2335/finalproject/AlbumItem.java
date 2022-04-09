package com.cst2335.finalproject;

public class AlbumItem {

    private String title;
    private String key_id;
    private String favStatus;
    private String artistId;

    public AlbumItem() {
    }

    public AlbumItem( String title, String key_id, String favStatus, String artistId) {
        this.title = title;
        this.key_id = key_id;
        this.favStatus = favStatus;
        this.artistId = artistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }
}