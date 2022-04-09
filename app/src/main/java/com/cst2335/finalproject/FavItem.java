package com.cst2335.finalproject;

public class FavItem {

    private String item_title;
    private String key_id;

    public FavItem() {
    }

    public FavItem(String item_title, String key_id) {
        this.item_title = item_title;
        this.key_id = key_id;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }
}
