package com.koders.budgie.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("tag_line")
    private String tagLine;
    @SerializedName("image")
    private String image;
    @SerializedName("country")
    private String country;
    @SerializedName("is_verified")
    private Boolean isVerified;

    public User(String email, String username, String firstName, String lastName, String tagLine, String image, String country) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tagLine = tagLine;
        this.image = image;
        this.country = country;
    }

    public User(String email, String username, String firstName, String lastName, String tagLine, String image, String country, Boolean isVerified) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tagLine = tagLine;
        this.image = image;
        this.country = country;
        this.isVerified = isVerified;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getImage() {
        return image;
    }

    public String getCountry() {
        return country;
    }

    public Boolean getVerified() {
        return isVerified;
    }
}
