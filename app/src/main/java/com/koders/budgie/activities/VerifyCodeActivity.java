package com.koders.budgie.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.koders.budgie.R;
import com.koders.budgie.model.Data;
import com.koders.budgie.model.User;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharedPreferencesHandler;
import com.koders.budgie.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCodeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarText, resendCode, time;
    private CountDownTimer countDownTimer;
    private ApiCall apiCall;
    private TextInputLayout verifyCodeInputLayout;
    private EditText verifyCode;
    private Button verifyBtn;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        init();

        startTimer();

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode.setText("");
                resendCode();
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidCode(verifyCode.getText().toString().trim())) {
                    //Toast.makeText(VerifyCodeActivity.this, "Valid", Toast.LENGTH_SHORT).show();
                    verifyCode();
                } else {
                    Toast.makeText(VerifyCodeActivity.this, "In Valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        resendCode = findViewById(R.id.resendCode);
        time = findViewById(R.id.time);
        verifyCodeInputLayout = findViewById(R.id.verifyCodeInputLayout);
        verifyCode = findViewById(R.id.verifyCode);
        verifyBtn = findViewById(R.id.verifyBtn);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarText.setText("Verify");

        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);
        loadingDialog = new LoadingDialog(VerifyCodeActivity.this);
    }

    private void startTimer() {

        disableResendCode();
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished / 1000 < 10) {
                    time.setText("00:0" + millisUntilFinished / 1000);
                } else {
                    time.setText("00:" + millisUntilFinished / 1000);
                }
            }

            @Override
            public void onFinish() {
                time.setText("00:00");
                enableResendCode();
            }
        }.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    private void disableResendCode() {
        resendCode.setEnabled(false);
        Spannable spannable = new SpannableString(getResources().getString(R.string.resend_code));
        spannable.setSpan(new ForegroundColorSpan(Color.argb(100, 128, 128, 128)), 26, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        resendCode.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    private void enableResendCode() {
        resendCode.setEnabled(true);
//        resendCode.setTextColor(Color.argb(255, 128, 128, 128));
        Spannable spannable = new SpannableString(getResources().getString(R.string.resend_code));
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 26, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        resendCode.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    private void verifyCode() {
        if (Utility.isNetworkConnected()) {
            loadingDialog.showLoading();
            apiCall.verifyCode("Token " + SharedPreferencesHandler.getToken(), verifyCode.getText().toString().trim()).enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {

                            User user = response.body().getUser();

                            if (response.body().isStatus()) {
                                loadingDialog.dismiss();

                                SharedPreferencesHandler.setEmail(user.getEmail());
                                SharedPreferencesHandler.setUsername(user.getUsername());
                                SharedPreferencesHandler.setFirstName(user.getFirstName());
                                SharedPreferencesHandler.setLastName(user.getLastName());
                                SharedPreferencesHandler.setCountry(user.getCountry());
                                SharedPreferencesHandler.setImage(user.getImage());
                                SharedPreferencesHandler.setTagLine(user.getTagLine());
                                SharedPreferencesHandler.setIsLogin(true);

                                Intent intent = new Intent(VerifyCodeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finishAffinity();

                            } else {
                                loadingDialog.dismiss();
                                Toast.makeText(VerifyCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(VerifyCodeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    loadingDialog.dismiss();
                    Toast.makeText(VerifyCodeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendCode() {
        if (Utility.isNetworkConnected()) {
            loadingDialog.showLoading();
            startTimer();
            apiCall.resendCode("Token " + SharedPreferencesHandler.getToken()).enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {

                            if (response.body().isStatus()) {
                                loadingDialog.dismiss();
                                Toast.makeText(VerifyCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                loadingDialog.dismiss();
                                Toast.makeText(VerifyCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(VerifyCodeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    loadingDialog.dismiss();
                    Toast.makeText(VerifyCodeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCode(String verifyCode) {
        if (verifyCode.isEmpty()) {
            verifyCodeInputLayout.setError("Code should not be empty");
            return false;
        } else if (verifyCode.length() < 6) {
            verifyCodeInputLayout.setError("Code should not be less than 6 digits");
            return false;
        } else {
            verifyCodeInputLayout.setError(null);
            verifyCodeInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}