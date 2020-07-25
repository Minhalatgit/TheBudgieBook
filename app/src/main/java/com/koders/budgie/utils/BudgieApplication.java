package com.koders.budgie.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class BudgieApplication extends Application {

    public static BudgieApplication instance;

    public static Context getCtx() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //init places sdk
        Places.initialize(this, "AIzaSyBP0656jkqdbjvYks2EMGNyGrosYSZUwIc");
        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);
    }
}
