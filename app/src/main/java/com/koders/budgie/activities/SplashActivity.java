package com.koders.budgie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.koders.budgie.R;
import com.koders.budgie.utils.SharedPreferencesHandler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);

        ImageView splashImage = findViewById(R.id.splashImage);
        TextView splashText = findViewById(R.id.splashText);

        splashImage.setAnimation(animation);
        splashText.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesHandler.getIsLogin()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);

    }
}
