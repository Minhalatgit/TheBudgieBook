package com.koders.budgie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.koders.budgie.R;
import com.koders.budgie.model.User;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.model.Data;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharedPreferencesHandler;
import com.koders.budgie.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView registerLabel, forgetPasswordLabel;
    private EditText emailText, passwordText;
    private Button loginUser;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private static final String TAG = "LoginActivity";
    LoadingDialog loadingDialog;
    ApiCall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        registerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(LoginActivity.this);
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgetPasswordLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(LoginActivity.this);

                String email = emailText.getText().toString().trim().toLowerCase();
                String password = passwordText.getText().toString().trim();

                if (!isEmailValid(email) | !isPasswordValid(password)) {
                    Log.d(TAG, "onClick: email or password is empty");
                } else {
                    if (Utility.isNetworkConnected()) {
                        loginUser(email, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void init() {
        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);

        forgetPasswordLabel = findViewById(R.id.forgotPasswordLabel);
        registerLabel = findViewById(R.id.registerLabel);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginUser = findViewById(R.id.loginButton);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        loadingDialog = new LoadingDialog(LoginActivity.this);
    }

    private void loginUser(String email, String password) {
        loadingDialog.showLoading();

        apiCall.loginUser(email, password).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        User user = response.body().getUser();

                        if (response.body().isStatus()) {
                            loadingDialog.dismiss();

                            String message = response.body().getMessage();
                            String token = response.body().getToken();
                            Log.d("Response", message);

                            SharedPreferencesHandler.setIsLogin(true);
                            SharedPreferencesHandler.setToken(token);

                            SharedPreferencesHandler.setEmail(user.getEmail());
                            SharedPreferencesHandler.setUsername(user.getUsername());
                            SharedPreferencesHandler.setFirstName(user.getFirstName());
                            SharedPreferencesHandler.setLastName(user.getLastName());
                            SharedPreferencesHandler.setCountry(user.getCountry());
                            SharedPreferencesHandler.setImage(user.getImage());
                            SharedPreferencesHandler.setTagLine(user.getTagLine());

                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();

                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                loadingDialog.dismiss();
                Log.d("Response", t.getMessage());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            passwordInputLayout.setError("Password must not be empty");
            return false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Password must contain at least 6 characters");
            return false;
        } else {
            passwordInputLayout.setError(null);
            return true;
        }
    }

}
