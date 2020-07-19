package com.koders.budgie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.koders.budgie.config.Constants;

public class SharedPreferencesHandler {

    private static SharedPreferences preferences = BudgieApplication.getCtx().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = preferences.edit();

    public static void setIsLogin(boolean isLogin) {
        editor.putBoolean(Constants.IS_LOGIN, isLogin);
        editor.commit();
    }

    public static boolean getIsLogin() {
        return preferences.getBoolean(Constants.IS_LOGIN, false);
    }

    public static void setToken(String token) {
        editor.putString(Constants.TOKEN, token);
        editor.commit();
    }

    public static String getToken() {
        return preferences.getString(Constants.TOKEN, "");
    }

    public static void setEmail(String email) {
        editor.putString(Constants.EMAIL, email);
        editor.commit();
    }

    public static String getEmail() {
        return preferences.getString(Constants.EMAIL, "");
    }

    public static void setUsername(String username) {
        editor.putString(Constants.USERNAME, username);
        editor.commit();
    }

    public static String getUsername() {
        return preferences.getString(Constants.USERNAME, "");
    }

    public static void setFirstName(String firstName) {
        editor.putString(Constants.FIRST_NAME, firstName);
        editor.commit();
    }

    public static String getFirstName() {
        return preferences.getString(Constants.FIRST_NAME, "");
    }

    public static void setLastName(String lastName) {
        editor.putString(Constants.LAST_NAME, lastName);
        editor.commit();
    }

    public static String getLastName() {
        return preferences.getString(Constants.LAST_NAME, "");
    }

    public static void setTagLine(String tagLine) {
        editor.putString(Constants.TAG_LINE, tagLine);
        editor.commit();
    }

    public static String getTagLine() {
        return preferences.getString(Constants.TAG_LINE, "");
    }

    public static void setImage(String image) {
        editor.putString(Constants.IMAGE, image);
        editor.commit();
    }

    public static String getImage() {
        return preferences.getString(Constants.IMAGE, "");
    }

    public static void setCountry(String country) {
        editor.putString(Constants.COUNTRY, country);
        editor.commit();
    }

    public static String getCountry() {
        return preferences.getString(Constants.COUNTRY, "");
    }

}
