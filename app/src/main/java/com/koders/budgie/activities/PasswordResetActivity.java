package com.koders.budgie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.koders.budgie.R;

public class PasswordResetActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout;
    private EditText emailText;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        init();
    }

    private void init() {
        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailText = findViewById(R.id.emailText);
        resetBtn = findViewById(R.id.resetBtn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            emailInputLayout.setError("Email must not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Please enter a valid email address");
            return false;
        } else {
            emailInputLayout.setError(null);
            return true;
        }
    }

}
