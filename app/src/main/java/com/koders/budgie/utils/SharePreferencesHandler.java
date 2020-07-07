package com.koders.budgie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.koders.budgie.config.Constants;

public class SharePreferencesHandler {

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
}
