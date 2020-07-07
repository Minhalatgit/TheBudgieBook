package com.koders.budgie.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BirdResponse {

    @SerializedName("status")
    private boolean status;

//    @SerializedName("total_birds")
    @SerializedName("count")
    private int totalBirds;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    //    @SerializedName("birds")
    @SerializedName("results")
    private List<BirdInfo> birdInfoList;

    public BirdResponse(boolean status, List<BirdInfo> birdInfoList, int totalBirds) {
        this.status = status;
        this.birdInfoList = birdInfoList;
        this.totalBirds = totalBirds;
    }

    public BirdResponse(boolean status, int totalBirds, String next, String previous, List<BirdInfo> birdInfoList) {
        this.status = status;
        this.totalBirds = totalBirds;
        this.next = next;
        this.previous = previous;
        this.birdInfoList = birdInfoList;
    }

    public boolean isStatus() {
        return status;
    }

    public List<BirdInfo> getBirdInfoList() {
        return birdInfoList;
    }

    public int getTotalBirds() {
        return totalBirds;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }
}
