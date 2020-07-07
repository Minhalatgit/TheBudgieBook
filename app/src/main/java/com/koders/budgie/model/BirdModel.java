package com.koders.budgie.model;

import com.google.gson.annotations.SerializedName;

public class BirdModel {

    @SerializedName("status")
    private boolean status;
    @SerializedName("bird")
    private BirdInfo birdInfo;

    public BirdModel(boolean status, BirdInfo birdInfo) {
        this.status = status;
        this.birdInfo = birdInfo;
    }

    public boolean isStatus() {
        return status;
    }

    public BirdInfo getBirdInfo() {
        return birdInfo;
    }
}
