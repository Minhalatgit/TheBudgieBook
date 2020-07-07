package com.koders.budgie.model;

public class Navigation {

    private String name;
    private int imageUrl;

    public Navigation(String name, int imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getImageUrl() {
        return imageUrl;
    }
}
