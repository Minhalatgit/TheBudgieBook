package com.koders.budgie.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private User user;

    public Profile(boolean status, User user) {
        this.status = status;
        this.user = user;
    }

    public boolean getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}
