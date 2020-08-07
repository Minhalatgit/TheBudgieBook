package com.koders.budgie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.koders.budgie.R;
import com.koders.budgie.config.Constants;
import com.koders.budgie.utils.SharedPreferencesHandler;

public class ProfilePictureActivity extends AppCompatActivity {

    ImageView profileImage, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        profileImage = findViewById(R.id.profileImage);
        cancel = findViewById(R.id.cancel);

        Glide.with(this)
                .load(Constants.BASE_URL + SharedPreferencesHandler.getImage())
                .placeholder(getDrawable(R.drawable.profile_image))
                .into(profileImage);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}