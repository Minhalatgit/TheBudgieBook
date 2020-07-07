package com.koders.budgie.model;

import com.google.gson.annotations.SerializedName;

public class BirdInfo {

    @SerializedName("Ring_no")
    private String ringNumber;

    @SerializedName("Sex")
    private String sex;

    @SerializedName("Hatch_date")
    private String hatchDate;

    @SerializedName("Arrival_date")
    private String arrivalDate;

    @SerializedName("Approx_age")
    private String approxAge;

    @SerializedName("Size")
    private String size;

    @SerializedName("Color")
    private String color;

    @SerializedName("Crested")
    private String crested;

    @SerializedName("Father")
    private String father;

    @SerializedName("Mother")
    private String mother;

    @SerializedName("Status")
    private String status;

    @SerializedName("Cage_number")
    private String cageNumber;

    @SerializedName("Ring_owner_name")
    private String ringOwnerName;

    @SerializedName("Purchased_price")
    private String purchasedPrice;

    @SerializedName("Taken_from")
    private String takenFrom;

    @SerializedName("Taken_date")
    private String takenDate;

    @SerializedName("Seller_number")
    private String sellerNumber;

    @SerializedName("Seller_Location")
    private String sellerLocation;

    @SerializedName("Selling_price")
    private String sellingPrice;

    @SerializedName("Given_to")
    private String givenTo;

    @SerializedName("Given_date")
    private String givenDate;

    @SerializedName("Buyer_number")
    private String buyerNumber;

    @SerializedName("Buyer_Location")
    private String buyerLocation;

    @SerializedName("With_partnership")
    private String withPartnership;

    @SerializedName("image")
    private String image;

    @SerializedName("Mutation")
    private String mutation;

    public BirdInfo() {
    }

    public BirdInfo(String ringNumber, String sex, String hatchDate, String arrivalDate, String approxAge, String size, String color, String crested, String father, String mother, String status, String cageNumber, String ringOwnerName, String purchasedPrice, String takenFrom, String takenDate, String sellerNumber, String sellerLocation, String sellingPrice, String givenTo, String givenDate, String buyerNumber, String buyerLocation, String withPartnership, String image, String mutation) {
        this.ringNumber = ringNumber;
        this.sex = sex;
        this.hatchDate = hatchDate;
        this.arrivalDate = arrivalDate;
        this.approxAge = approxAge;
        this.size = size;
        this.color = color;
        this.crested = crested;
        this.father = father;
        this.mother = mother;
        this.status = status;
        this.cageNumber = cageNumber;
        this.ringOwnerName = ringOwnerName;
        this.purchasedPrice = purchasedPrice;
        this.takenFrom = takenFrom;
        this.takenDate = takenDate;
        this.sellerNumber = sellerNumber;
        this.sellerLocation = sellerLocation;
        this.sellingPrice = sellingPrice;
        this.givenTo = givenTo;
        this.givenDate = givenDate;
        this.buyerNumber = buyerNumber;
        this.buyerLocation = buyerLocation;
        this.withPartnership = withPartnership;
        this.image = image;
        this.mutation = mutation;
    }

    public String getRingNumber() {
        return ringNumber;
    }

    public String getSex() {
        return sex;
    }

    public String getHatchDate() {
        return hatchDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getApproxAge() {
        return approxAge;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getCrested() {
        return crested;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getStatus() {
        return status;
    }

    public String getCageNumber() {
        return cageNumber;
    }

    public String getRingOwnerName() {
        return ringOwnerName;
    }

    public String getPurchasedPrice() {
        return purchasedPrice;
    }

    public String getTakenFrom() {
        return takenFrom;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public String getSellerNumber() {
        return sellerNumber;
    }

    public String getSellerLocation() {
        return sellerLocation;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public String getGivenTo() {
        return givenTo;
    }

    public String getGivenDate() {
        return givenDate;
    }

    public String getBuyerNumber() {
        return buyerNumber;
    }

    public String getBuyerLocation() {
        return buyerLocation;
    }

    public String getWithPartnership() {
        return withPartnership;
    }

    public String getImage() {
        return image;
    }

    public String getMutation() {
        return mutation;
    }

    public void setRingNumber(String ringNumber) {
        this.ringNumber = ringNumber;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setHatchDate(String hatchDate) {
        this.hatchDate = hatchDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setApproxAge(String approxAge) {
        this.approxAge = approxAge;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCrested(String crested) {
        this.crested = crested;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCageNumber(String cageNumber) {
        this.cageNumber = cageNumber;
    }

    public void setRingOwnerName(String ringOwnerName) {
        this.ringOwnerName = ringOwnerName;
    }

    public void setPurchasedPrice(String purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public void setTakenFrom(String takenFrom) {
        this.takenFrom = takenFrom;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    public void setSellerNumber(String sellerNumber) {
        this.sellerNumber = sellerNumber;
    }

    public void setSellerLocation(String sellerLocation) {
        this.sellerLocation = sellerLocation;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setGivenTo(String givenTo) {
        this.givenTo = givenTo;
    }

    public void setGivenDate(String givenDate) {
        this.givenDate = givenDate;
    }

    public void setBuyerNumber(String buyerNumber) {
        this.buyerNumber = buyerNumber;
    }

    public void setBuyerLocation(String buyerLocation) {
        this.buyerLocation = buyerLocation;
    }

    public void setWithPartnership(String withPartnership) {
        this.withPartnership = withPartnership;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMutation(String mutation) {
        this.mutation = mutation;
    }
}
