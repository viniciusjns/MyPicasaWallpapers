package com.example.vinicius_pc.mypicasawallpapers.model;

import java.io.Serializable;

/**
 * Created by Vinicius-PC on 18/04/2016.
 */
public class Wallpaper implements Serializable {
    private static final long serialVersionUID = 1L;
    private String url;
    private String description;

    public Wallpaper() {
    }

    public Wallpaper(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
