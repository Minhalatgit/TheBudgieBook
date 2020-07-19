package com.koders.budgie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koders.budgie.R;
import com.koders.budgie.config.Constants;
import com.koders.budgie.model.BirdInfo;
import com.koders.budgie.model.Data;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.model.BirdModel;
import com.koders.budgie.utils.BirdInfoDialog;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharedPreferencesHandler;
import com.koders.budgie.utils.Utility;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;

public class BirdDetailActivity extends AppCompatActivity {

    private ImageView deleteBird, editBird, mainPlus, fatherPlus, motherPlus, grandFatherLeftPlus, grandMotherLeftPlus, grandFatherRightPlus, grandMotherRightPlus;
    private CircleImageView topImage, fatherImage, grandFatherImageLeft, grandMotherImageLeft, motherImage, grandFatherImageRight,
            grandMotherImageRight;
    private TextView topMutation, topRingNum, topAge, topGender, fatherRingNum, fatherMutation, grandFatherRingNumLeft, grandFatherMutationLeft,
            grandMotherRingNumLeft, grandMotherMutationLeft, motherRingNum, motherMutation, grandFatherRingNumRight, grandFatherMutationRight,
            grandMotherRingNumRight, grandMotherMutationRight;
    private LinearLayout fatherCard, motherCard, grandFatherCardLeft, grandMotherCardLeft, grandFatherCardRight, grandMotherCardRight;
    private RelativeLayout mainCard, fatherMainCard, motherMainCard;
    private LoadingDialog loadingDialog;
    private ProgressBar mainProgress, fatherProgress, motherProgress, grandFatherLeftProgress, grandMotherLeftProgress, grandFatherRightProgress, grandMotherRightProgress;
    private ApiCall apiCall;
    String ringNumMain = "", mainCardDetails, fatherCardDetails, motherCardDetails, grandFatherLeftCardDetails, grandMotherLeftCardDetails,
            grandFatherRightCardDetails, grandMotherRightCardDetails;
    private AlertDialog.Builder dialog;

    private BirdInfoDialog birdInfoDialog;
    private BirdInfo mainBirdInfo, fatherBirdInfo, grandFatherLeftBirdInfo, grandMotherLeftBirdInfo, motherBirdInfo, grandFatherRightBirdInfo, grandMotherRightBirdInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_detail);

        if (getIntent().hasExtra("ringNum")) {
            ringNumMain = getIntent().getStringExtra("ringNum");
        }

        init();

        if (Utility.isNetworkConnected()) {
            getBird(ringNumMain);
        } else {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }

        deleteBird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBird(ringNumMain);
            }
        });

        editBird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBird(ringNumMain);
            }
        });

        fatherMainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBird(fatherBirdInfo.getRingNumber());
            }
        });

        motherMainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBird(motherBirdInfo.getRingNumber());
            }
        });

        grandFatherCardLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBird(grandFatherLeftBirdInfo.getRingNumber());
            }
        });

        grandMotherCardLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBird(grandMotherLeftBirdInfo.getRingNumber());
            }
        });

        grandFatherCardRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBird(grandFatherRightBirdInfo.getRingNumber());
            }
        });

        grandMotherCardRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBird(grandMotherRightBirdInfo.getRingNumber());
            }
        });

        mainPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdInfoDialog.showDialog(mainBirdInfo);
            }
        });

        fatherPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdInfoDialog.showDialog(fatherBirdInfo);
            }
        });

        motherPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdInfoDialog.showDialog(motherBirdInfo);
            }
        });
        grandFatherLeftPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdInfoDialog.showDialog(grandFatherLeftBirdInfo);
            }
        });
        grandMotherLeftPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdInfoDialog.showDialog(grandMotherLeftBirdInfo);
            }
        });
        grandFatherRightPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdInfoDialog.showDialog(grandFatherRightBirdInfo);
            }
        });
        grandMotherRightPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdInfoDialog.showDialog(grandMotherRightBirdInfo);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBird(ringNumMain);
    }

    private void init() {
        deleteBird = findViewById(R.id.delete_bird);
        editBird = findViewById(R.id.edit_bird);

        topImage = findViewById(R.id.topImage);
        fatherImage = findViewById(R.id.fatherImage);
        motherImage = findViewById(R.id.motherImage);
        grandFatherImageLeft = findViewById(R.id.grandFatherImageLeft);
        grandMotherImageLeft = findViewById(R.id.grandMotherImageLeft);
        grandFatherImageRight = findViewById(R.id.grandFatherImageRight);
        grandMotherImageRight = findViewById(R.id.grandMotherImageRight);

        topMutation = findViewById(R.id.topMutation);
        topRingNum = findViewById(R.id.topRingNum);
        topAge = findViewById(R.id.topAge);
        topGender = findViewById(R.id.topGender);
        fatherRingNum = findViewById(R.id.fatherRingNum);
        fatherMutation = findViewById(R.id.fatherMutation);
        grandFatherRingNumLeft = findViewById(R.id.grandFatherRingNumLeft);
        grandFatherMutationLeft = findViewById(R.id.grandFatherMutationLeft);
        grandMotherRingNumLeft = findViewById(R.id.grandMotherRingNumLeft);
        grandMotherMutationLeft = findViewById(R.id.grandMotherMutationLeft);
        motherRingNum = findViewById(R.id.motherRingNum);
        motherMutation = findViewById(R.id.motherMutation);
        grandFatherRingNumRight = findViewById(R.id.grandFatherRingNumRight);
        grandFatherMutationRight = findViewById(R.id.grandFatherMutationRight);
        grandMotherRingNumRight = findViewById(R.id.grandMotherRingNumRight);
        grandMotherMutationRight = findViewById(R.id.grandMotherMutationRight);
        grandFatherCardLeft = findViewById(R.id.grandFatherCardLeft);
        grandMotherCardLeft = findViewById(R.id.grandMotherCardLeft);
        grandFatherCardRight = findViewById(R.id.grandFatherCardRight);
        grandMotherCardRight = findViewById(R.id.grandMotherCardRight);
        fatherMainCard = findViewById(R.id.fatherMainCard);
        motherMainCard = findViewById(R.id.motherMainCard);
        fatherCard = findViewById(R.id.fatherCard);
        motherCard = findViewById(R.id.motherCard);
        mainCard = findViewById(R.id.mainCard);

        mainProgress = findViewById(R.id.mainProgress);
        fatherProgress = findViewById(R.id.fatherProgress);
        motherProgress = findViewById(R.id.motherProgress);
        grandFatherLeftProgress = findViewById(R.id.grandFatherLeftProgress);
        grandMotherLeftProgress = findViewById(R.id.grandMotherLeftProgress);
        grandFatherRightProgress = findViewById(R.id.grandFatherRightProgress);
        grandMotherRightProgress = findViewById(R.id.grandMotherRightProgress);

        mainPlus = findViewById(R.id.mainPlus);
        fatherPlus = findViewById(R.id.fatherPlus);
        motherPlus = findViewById(R.id.motherPlus);
        grandFatherLeftPlus = findViewById(R.id.grandFatherLeftPlus);
        grandMotherLeftPlus = findViewById(R.id.grandMotherLeftPlus);
        grandFatherRightPlus = findViewById(R.id.grandFatherRightPlus);
        grandMotherRightPlus = findViewById(R.id.grandMotherRightPlus);

        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);
        loadingDialog = new LoadingDialog(BirdDetailActivity.this);
        birdInfoDialog = new BirdInfoDialog(BirdDetailActivity.this);
    }

    private void getBird(String ringNum) {
        mainProgress.setVisibility(View.VISIBLE);
        apiCall.getBird(ringNum).enqueue(new Callback<BirdModel>() {
            @Override
            public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            mainProgress.setVisibility(View.GONE);
                            mainBirdInfo = response.body().getBirdInfo();

                            mainCardDetails = mainBirdInfo.getRingNumber() + "\n" + mainBirdInfo.getRingOwnerName();

                            Glide.with(getApplicationContext()).load(Constants.BASE_URL + mainBirdInfo.getImage()).placeholder(R.drawable.bird).into(topImage);
                            topRingNum.setText(mainBirdInfo.getRingNumber());
                            if (mainBirdInfo.getApproxAge() == null || mainBirdInfo.getApproxAge().equals("")) {
                                topAge.setText("---");
                            } else {
                                topAge.setText(mainBirdInfo.getApproxAge());
                            }
                            if (mainBirdInfo.getMutation() == null || mainBirdInfo.getMutation().equals("")) {
                                topMutation.setText("\t----");
                            } else {
                                topMutation.setText(mainBirdInfo.getMutation());
                            }
                            if (mainBirdInfo.getSex() == null || mainBirdInfo.getSex().equals("")) {
                                topGender.setText("\t----");
                            } else {
                                topGender.setText(mainBirdInfo.getSex());
                            }

                            //father
                            if (mainBirdInfo.getFather() == null || mainBirdInfo.getFather().equals("")) {
                                fatherCard.setVisibility(View.GONE);
                            } else {
                                //fatherProgress.setVisibility(View.VISIBLE);
                                apiCall.getBird(mainBirdInfo.getFather()).enqueue(new Callback<BirdModel>() {
                                    @Override
                                    public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() != null) {

                                                if (response.body().isStatus()) {
                                                    fatherCard.setVisibility(View.VISIBLE);
                                                    fatherProgress.setVisibility(View.GONE);
                                                    fatherBirdInfo = response.body().getBirdInfo();

                                                    fatherCardDetails = fatherBirdInfo.getRingNumber() + "\n" + fatherBirdInfo.getRingOwnerName();

                                                    Glide.with(getApplicationContext()).load(Constants.BASE_URL + fatherBirdInfo.getImage()).placeholder(R.drawable.bird).into(fatherImage);
                                                    fatherRingNum.setText(fatherBirdInfo.getRingNumber());
                                                    if (fatherBirdInfo.getMutation() == null || fatherBirdInfo.getMutation().equals("")) {
                                                        fatherMutation.setText("\t----");
                                                    } else {
                                                        fatherMutation.setText(fatherBirdInfo.getMutation());
                                                    }
                                                    Log.d("Response", "Father response is True");

                                                    //grand father left
                                                    if (fatherBirdInfo.getFather() == null || fatherBirdInfo.getFather().equals("")) {
                                                        grandFatherCardLeft.setVisibility(View.GONE);
                                                    } else {
                                                        //grandFatherLeftProgress.setVisibility(View.VISIBLE);
                                                        apiCall.getBird(fatherBirdInfo.getFather()).enqueue(new Callback<BirdModel>() {
                                                            @Override
                                                            public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                                                                if (response.isSuccessful()) {
                                                                    if (response.body() != null) {

                                                                        if (response.body().isStatus()) {
                                                                            grandFatherCardLeft.setVisibility(View.VISIBLE);
                                                                            grandFatherLeftProgress.setVisibility(View.GONE);
                                                                            grandFatherLeftBirdInfo = response.body().getBirdInfo();

                                                                            grandFatherLeftCardDetails = grandFatherLeftBirdInfo.getRingNumber() + "\n" + grandFatherLeftBirdInfo.getRingOwnerName();

                                                                            Glide.with(getApplicationContext()).load(Constants.BASE_URL + grandFatherLeftBirdInfo.getImage()).placeholder(R.drawable.bird).into(grandFatherImageLeft);
                                                                            grandFatherRingNumLeft.setText(grandFatherLeftBirdInfo.getRingNumber());
                                                                            if (grandFatherLeftBirdInfo.getMutation() == null || grandFatherLeftBirdInfo.getMutation().equals("")) {
                                                                                grandFatherMutationLeft.setText("\t----");
                                                                            } else {
                                                                                grandFatherMutationLeft.setText(grandFatherLeftBirdInfo.getMutation());
                                                                            }
                                                                            Log.d("Response", "Grand father response is True");
                                                                        } else {
                                                                            grandFatherLeftProgress.setVisibility(View.GONE);
                                                                            grandFatherCardLeft.setVisibility(View.GONE);
                                                                            Log.d("Response", "Grand father response is False");
                                                                        }
                                                                    }
                                                                } else {
                                                                    Log.d("Response", response.message());
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<BirdModel> call, Throwable t) {
                                                                Log.d("Response", t.getMessage());
                                                            }
                                                        });
                                                    }

                                                    //grand mother left
                                                    if (fatherBirdInfo.getMother() == null || fatherBirdInfo.getMother().equals("")) {
                                                        grandMotherCardLeft.setVisibility(View.GONE);
                                                    } else {
                                                        //grandMotherLeftProgress.setVisibility(View.VISIBLE);
                                                        apiCall.getBird(fatherBirdInfo.getMother()).enqueue(new Callback<BirdModel>() {
                                                            @Override
                                                            public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                                                                if (response.isSuccessful()) {
                                                                    if (response.body() != null) {

                                                                        if (response.body().isStatus()) {
                                                                            grandMotherCardLeft.setVisibility(View.VISIBLE);
                                                                            grandMotherLeftProgress.setVisibility(View.GONE);
                                                                            grandMotherLeftBirdInfo = response.body().getBirdInfo();

                                                                            grandMotherLeftCardDetails = grandMotherLeftBirdInfo.getRingNumber() + "\n" + grandMotherLeftBirdInfo.getRingOwnerName();

                                                                            Glide.with(getApplicationContext()).load(Constants.BASE_URL + grandMotherLeftBirdInfo.getImage()).placeholder(R.drawable.bird).into(grandMotherImageLeft);
                                                                            grandMotherRingNumLeft.setText(grandMotherLeftBirdInfo.getRingNumber());
                                                                            if (grandMotherLeftBirdInfo.getMutation() == null || grandMotherLeftBirdInfo.getMutation().equals("")) {
                                                                                grandMotherMutationLeft.setText("\t----");
                                                                            } else {
                                                                                grandMotherMutationLeft.setText(grandMotherLeftBirdInfo.getMutation());
                                                                            }
                                                                            Log.d("Response", "Grand mother response is True");
                                                                        } else {
                                                                            grandMotherCardLeft.setVisibility(View.GONE);
                                                                            grandMotherLeftProgress.setVisibility(View.GONE);
                                                                            Log.d("Response", "Grand mother response is False");
                                                                        }
                                                                    }
                                                                } else {
                                                                    Log.d("Response", response.message());
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<BirdModel> call, Throwable t) {
                                                                Log.d("Response", t.getMessage());
                                                            }
                                                        });
                                                    }

                                                } else {
                                                    fatherCard.setVisibility(View.GONE);
                                                    fatherProgress.setVisibility(View.GONE);
                                                    Log.d("Response", "Father response is False");
                                                }
                                            }
                                        } else {
                                            Log.d("Response", response.message());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BirdModel> call, Throwable t) {
                                        Log.d("Response", t.getMessage());
                                    }
                                });
                            }

                            //mother
                            if (mainBirdInfo.getMother() == null || mainBirdInfo.getMother().equals("")) {
                                motherCard.setVisibility(View.GONE);
                            } else {
                                //motherProgress.setVisibility(View.VISIBLE);
                                apiCall.getBird(mainBirdInfo.getMother()).enqueue(new Callback<BirdModel>() {
                                    @Override
                                    public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() != null) {

                                                if (response.body().isStatus()) {
                                                    motherCard.setVisibility(View.VISIBLE);
                                                    motherProgress.setVisibility(View.GONE);
                                                    motherBirdInfo = response.body().getBirdInfo();

                                                    motherCardDetails = motherBirdInfo.getRingNumber() + "\n" + motherBirdInfo.getRingOwnerName();

                                                    Glide.with(getApplicationContext()).load(Constants.BASE_URL + motherBirdInfo.getImage()).placeholder(R.drawable.bird).into(motherImage);
                                                    motherRingNum.setText(motherBirdInfo.getRingNumber());
                                                    if (motherBirdInfo.getMutation() == null || motherBirdInfo.getMutation().equals("")) {
                                                        motherMutation.setText("\t----");
                                                    } else {
                                                        motherMutation.setText(motherBirdInfo.getMutation());
                                                    }
                                                    Log.d("Response", "Mother response is True");

                                                    //grand father right
                                                    if (motherBirdInfo.getFather() == null || motherBirdInfo.getFather().equals("")) {
                                                        grandFatherCardRight.setVisibility(View.GONE);
                                                    } else {
                                                        //grandFatherRightProgress.setVisibility(View.VISIBLE);
                                                        apiCall.getBird(motherBirdInfo.getFather()).enqueue(new Callback<BirdModel>() {
                                                            @Override
                                                            public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                                                                if (response.isSuccessful()) {
                                                                    if (response.body() != null) {

                                                                        if (response.body().isStatus()) {
                                                                            grandFatherRightProgress.setVisibility(View.GONE);
                                                                            grandFatherCardRight.setVisibility(View.VISIBLE);
                                                                            grandFatherRightBirdInfo = response.body().getBirdInfo();

                                                                            grandFatherRightCardDetails = grandFatherRightBirdInfo.getRingNumber() + "\n" + grandFatherRightBirdInfo.getRingOwnerName();

                                                                            Glide.with(getApplicationContext()).load(Constants.BASE_URL + grandFatherRightBirdInfo.getImage()).placeholder(R.drawable.bird).into(grandFatherImageRight);
                                                                            grandFatherRingNumRight.setText(grandFatherRightBirdInfo.getRingNumber());
                                                                            if (grandFatherRightBirdInfo.getMutation() == null || grandFatherRightBirdInfo.getMutation().equals("")) {
                                                                                grandFatherMutationRight.setText("\t----");
                                                                            } else {
                                                                                grandFatherMutationRight.setText(grandFatherRightBirdInfo.getMutation());
                                                                            }
                                                                            Log.d("Response", "Grand father response is True");
                                                                        } else {
                                                                            grandFatherCardRight.setVisibility(View.GONE);
                                                                            grandFatherRightProgress.setVisibility(View.GONE);
                                                                            Log.d("Response", "Grand father response is False");
                                                                        }
                                                                    }
                                                                } else {
                                                                    Log.d("Response", response.message());
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<BirdModel> call, Throwable t) {
                                                                Log.d("Response", t.getMessage());
                                                            }
                                                        });
                                                    }

                                                    //grand mother right
                                                    if (motherBirdInfo.getMother() == null || motherBirdInfo.getMother().equals("")) {
                                                        grandMotherCardRight.setVisibility(View.GONE);
                                                    } else {
                                                        // grandMotherRightProgress.setVisibility(View.VISIBLE);
                                                        apiCall.getBird(motherBirdInfo.getMother()).enqueue(new Callback<BirdModel>() {
                                                            @Override
                                                            public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                                                                if (response.isSuccessful()) {
                                                                    if (response.body() != null) {

                                                                        if (response.body().isStatus()) {
                                                                            grandMotherRightProgress.setVisibility(View.GONE);
                                                                            grandMotherCardRight.setVisibility(View.VISIBLE);
                                                                            grandMotherRightBirdInfo = response.body().getBirdInfo();

                                                                            grandMotherRightCardDetails = grandMotherRightBirdInfo.getRingNumber() + "\n" + grandMotherRightBirdInfo.getRingOwnerName();

                                                                            Glide.with(getApplicationContext()).load(Constants.BASE_URL + grandMotherRightBirdInfo.getImage()).placeholder(R.drawable.bird).into(grandMotherImageRight);
                                                                            grandMotherRingNumRight.setText(grandMotherRightBirdInfo.getRingNumber());
                                                                            if (grandMotherRightBirdInfo.getMutation() == null || grandMotherRightBirdInfo.getMutation().equals("")) {
                                                                                grandMotherMutationRight.setText("\t----");
                                                                            } else {
                                                                                grandMotherMutationRight.setText(grandMotherRightBirdInfo.getMutation());
                                                                            }
                                                                            Log.d("Response", "Grand mother response is True");
                                                                        } else {
                                                                            grandMotherCardRight.setVisibility(View.GONE);
                                                                            grandMotherRightProgress.setVisibility(View.GONE);
                                                                            Log.d("Response", "Grand mother response is False");
                                                                        }
                                                                    }
                                                                } else {
                                                                    Log.d("Response", response.message());
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<BirdModel> call, Throwable t) {
                                                                Log.d("Response", t.getMessage());
                                                            }
                                                        });
                                                    }

                                                } else {
                                                    motherCard.setVisibility(View.GONE);
                                                    motherProgress.setVisibility(View.GONE);
                                                    Log.d("Response", "Mother response is False");
                                                }
                                            }
                                        } else {
                                            Log.d("Response", response.message());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BirdModel> call, Throwable t) {
                                        Log.d("Response", t.getMessage());
                                    }
                                });
                            }
                        } else {
                            Log.d("Response", "Main bird response is false");
                        }

                    }
                } else {
                    Log.d("Response", response.message());
                }
            }

            @Override
            public void onFailure(Call<BirdModel> call, Throwable t) {
                Log.d("Response", t.getMessage());
            }
        });
    }

    private void deleteBird(String ringNum) {
        AlertDialog.Builder deleteAlert = new AlertDialog.Builder(BirdDetailActivity.this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete api call
                        loadingDialog.showLoading();
                        apiCall.deleteBird("Token " + SharedPreferencesHandler.getToken(), ringNum).enqueue(new Callback<Data>() {
                            @Override
                            public void onResponse(Call<Data> call, Response<Data> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {

                                        if (response.body().isStatus()) {
                                            loadingDialog.dismiss();
                                            String message = response.body().getMessage();
                                            Toast.makeText(BirdDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                                            finish();

                                            Log.d("Response", message);
                                        } else {
                                            loadingDialog.dismiss();
                                            Log.d("Response", response.body().getMessage());
                                        }

                                    }
                                } else {
                                    loadingDialog.dismiss();
                                    Log.d("Response", response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Data> call, Throwable t) {
                                loadingDialog.dismiss();
                                Log.d("Response", t.getMessage());
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = deleteAlert.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(BLACK);

    }

    private void editBird(String ringNum) {
        Intent intent = new Intent(BirdDetailActivity.this, AddBirdActivity.class);
        intent.putExtra("from", "edit");
        intent.putExtra("ringNum", ringNum);
        startActivity(intent);
    }
}
