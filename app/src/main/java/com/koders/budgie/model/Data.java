package com.koders.budgie.model;

import com.google.gson.annotations.SerializedName;

public class Data {

    private boolean status;
    private String message;
    private String token;
    private boolean mail_sent;
    @SerializedName("data")
    private User user;

    public Data(boolean status, String message, String token, User user) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public boolean isMail_sent() {
        return mail_sent;
    }
}
