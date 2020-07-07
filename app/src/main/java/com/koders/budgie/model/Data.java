package com.koders.budgie.model;

public class Data {

    private boolean status;
    private String message;
    private String token;

    public Data(boolean status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
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
}
