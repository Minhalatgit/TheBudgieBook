package com.koders.budgie.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.koders.budgie.R;
import com.koders.budgie.config.Constants;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.model.BirdInfo;
import com.koders.budgie.model.BirdModel;
import com.koders.budgie.model.Data;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharedPreferencesHandler;
import com.koders.budgie.utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBirdActivity extends AppCompatActivity {

    //views
    private Spinner sexSpinner, sizeSpinner, crestedSpinner;

    private TextInputLayout ringNumberInputLayout, hatchDateInputLayout, arrivalDateInputLayout, approxAgeInputLayout, colorInputLayout,
            motherInputLayout, fatherInputLayout, statusInputLayout, cageNumberInputLayout, ringOwnerNameInputLayout, purchasedPriceInputLayout,
            takenFromInputLayout, takenDateInputLayout, sellerNumberInputLayout, sellerLocationInputLayout, sellingPriceInputLayout, givenToInputLayout,
            givenDateInputLayout, buyerNumberInputLayout, buyerLocationInputLayout, withPartnershipInputLayout, mutationInputLayout;

    private EditText ringNumberText, hatchDateText, arrivalDateText, approxAgeText, colorText,
            motherText, fatherText, statusText, cageNumberText, ringOwnerNameText, purchasedPriceText, takenFromText,
            takenDateText, sellerNumberText, sellerLocationText, sellingPriceText, givenToText, givenDateText, buyerNumberText,
            buyerLocationText, withPartnershipText, mutationText;

    private TextView title;
    private ImageView mutationBtn;
    private Button submitBtn;
    private CircleImageView bird_profile_image;
    private ScrollView scrollView;

    //adapters
    ArrayAdapter<CharSequence> sexAdapter, sizeAdapter, crestedAdapter;

    //date picker
    private DatePickerDialog hatchDatePickerDialog, arrivalDatePickerDialog, takenDatePickerDialog, givenDatePickerDialog;
    private DatePickerDialog.OnDateSetListener hatchDateListener, arrivalDateListener, takenDateListener, givenDateListener;
    private Calendar calendar;
    private int year, month, day;

    //mutation list arrays
    String[] mutationList;
    boolean[] checkedItems;
    ArrayList<Integer> selectedItems = new ArrayList<>();

    private static final int GALLERY_REQUEST_CODE = 10;
    private static final int CAMERA_REQUEST_CODE = 20;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    //Api
    ApiCall apiCall;

    //progress dialog
    LoadingDialog loadingDialog;

    String from = "", ringNum = "";
    String picturePath;
    MultipartBody.Part image;
    boolean isRingNumValid, isRingOwnerNameValid;
    List<Place.Field> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bird);

        loadingDialog = new LoadingDialog(AddBirdActivity.this);
        init();

        if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
        }

        if (getIntent().hasExtra("ringNum")) {
            ringNum = getIntent().getStringExtra("ringNum");
        }

        if (from.equals("edit")) {
            title.setText("Edit Bird");
            getBird(ringNum);
            ringNumberText.setEnabled(false);
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        mutationList = getResources().getStringArray(R.array.mutation);
        checkedItems = new boolean[mutationList.length];

        mutationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBirdActivity.this)
                        .setTitle("Select Mutation")
                        .setMultiChoiceItems(mutationList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if (isChecked) {
                                    if (!selectedItems.contains(position)) {
                                        selectedItems.add(position);
                                    } else {
                                        selectedItems.remove(position);
                                    }
                                } else if (selectedItems.contains(position)) {
                                    selectedItems.remove(Integer.valueOf(position));
                                }
                            }
                        });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < selectedItems.size(); i++) {
                            item = item + mutationList[selectedItems.get(i)];

                            if (i != selectedItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        mutationText.setText(item);

                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            selectedItems.clear();
                            mutationText.setText("");
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
            }
        });

        //on clicking date fields -Start Region-
        hatchDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchDatePickerDialog = new DatePickerDialog(AddBirdActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        hatchDateListener,
                        year, month, day);
                hatchDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                hatchDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                hatchDatePickerDialog.show();
            }
        });

        arrivalDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrivalDatePickerDialog = new DatePickerDialog(AddBirdActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        arrivalDateListener,
                        year, month, day);
                arrivalDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                arrivalDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                arrivalDatePickerDialog.show();
            }
        });

        takenDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrivalDatePickerDialog = new DatePickerDialog(AddBirdActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        takenDateListener,
                        year, month, day);
                arrivalDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                arrivalDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                arrivalDatePickerDialog.show();
            }
        });

        givenDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrivalDatePickerDialog = new DatePickerDialog(AddBirdActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        givenDateListener,
                        year, month, day);
                arrivalDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                arrivalDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                arrivalDatePickerDialog.show();
            }
        });
        //on clicking date fields -End Region-

        //on clicking ok on date picker dialog-Start Region-
        hatchDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthOfYear = month + 1;
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                if (monthOfYear < 10) {

                    formattedMonth = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                String date = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                Log.d("Date", date);
                hatchDateText.setText(date);
            }
        };

        arrivalDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthOfYear = month + 1;
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                if (monthOfYear < 10) {

                    formattedMonth = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                String date = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                Log.d("Arrival Date", date);
                arrivalDateText.setText(date);
            }
        };
        takenDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthOfYear = month + 1;
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                if (monthOfYear < 10) {

                    formattedMonth = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                String date = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                Log.d("Arrival Date", date);
                takenDateText.setText(date);
            }
        };
        givenDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthOfYear = month + 1;
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                if (monthOfYear < 10) {

                    formattedMonth = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                String date = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                Log.d("ArrivalDate", date);
                givenDateText.setText(date);
            }
        };
        //on clicking ok on date picker dialog -End Region-

        bird_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ringNumber = ringNumberText.getText().toString().trim();
                String sex = sexSpinner.getSelectedItem().toString().trim();
                String hatchDate = hatchDateText.getText().toString().trim();
                String arrivalDate = arrivalDateText.getText().toString().trim();
                String approxAge = approxAgeText.getText().toString().trim();
                String size = sizeSpinner.getSelectedItem().toString().trim();
                String color = colorText.getText().toString().trim();
                String crested = crestedSpinner.getSelectedItem().toString().trim();
                String mother = motherText.getText().toString().trim();
                String father = fatherText.getText().toString().trim();
                String status = statusText.getText().toString().trim();
                String cageNumber = cageNumberText.getText().toString().trim();
                String ringOwnerName = ringOwnerNameText.getText().toString().trim();
                String purchasedPrice = purchasedPriceText.getText().toString().trim();
                String takenFrom = takenFromText.getText().toString().trim();
                String takenDate = takenDateText.getText().toString().trim();
                String sellerNumber = sellerNumberText.getText().toString().trim();
                String sellerLocation = sellerLocationText.getText().toString().trim();
                String sellingPrice = sellingPriceText.getText().toString().trim();
                String givenTo = givenToText.getText().toString().trim();
                String givenDate = givenDateText.getText().toString().trim();
                String buyerNumber = buyerNumberText.getText().toString().trim();
                String buyerLocation = buyerLocationText.getText().toString().trim();
                String withPartnership = withPartnershipText.getText().toString().trim();
                String mutation = mutationText.getText().toString().trim();

                if (Utility.isNetworkConnected()) {

                    if (!isRingNumberValid(ringNumber) | !isRingOwnerNameValid(ringOwnerName)) {
                        Log.d("valid", "yes");
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(200);
                        }
                        if (!isRingNumValid) {
                            ringNumberInputLayout.getParent().requestChildFocus(ringNumberInputLayout, ringNumberInputLayout);
                        } else if (!isRingOwnerNameValid) {
                            ringOwnerNameInputLayout.getParent().requestChildFocus(ringOwnerNameInputLayout, ringOwnerNameInputLayout);
                        }
                        //Toast.makeText(AddBirdActivity.this, "Invalid data", Toast.LENGTH_SHORT).show();
                    } else {

                        if (picturePath != null) {
                            File file = new File(picturePath);
                            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                            image = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
                        }
                        RequestBody ringNumberReq = RequestBody.create(MediaType.parse("multipart/form-data"), ringNumber);
                        RequestBody sexReq = RequestBody.create(MediaType.parse("text/plain"), sex);
                        RequestBody hatchDateReq = RequestBody.create(MediaType.parse("text/plain"), hatchDate);
                        RequestBody arrivalDateReq = RequestBody.create(MediaType.parse("text/plain"), arrivalDate);
                        RequestBody approxAgeReq = RequestBody.create(MediaType.parse("text/plain"), approxAge);
                        RequestBody sizeReq = RequestBody.create(MediaType.parse("text/plain"), size);
                        RequestBody colorReq = RequestBody.create(MediaType.parse("text/plain"), color);
                        RequestBody crestedReq = RequestBody.create(MediaType.parse("text/plain"), crested);
                        RequestBody fatherReq = RequestBody.create(MediaType.parse("text/plain"), father);
                        RequestBody motherReq = RequestBody.create(MediaType.parse("text/plain"), mother);
                        RequestBody statusReq = RequestBody.create(MediaType.parse("text/plain"), status);
                        RequestBody cageNumberReq = RequestBody.create(MediaType.parse("text/plain"), cageNumber);
                        RequestBody ringOwnerNameReq = RequestBody.create(MediaType.parse("text/plain"), ringOwnerName);
                        RequestBody purchasedPriceReq = RequestBody.create(MediaType.parse("text/plain"), purchasedPrice);
                        RequestBody takenFromReq = RequestBody.create(MediaType.parse("text/plain"), takenFrom);
                        RequestBody takenDateReq = RequestBody.create(MediaType.parse("text/plain"), takenDate);
                        RequestBody sellerNumberReq = RequestBody.create(MediaType.parse("text/plain"), sellerNumber);
                        RequestBody sellerLocationReq = RequestBody.create(MediaType.parse("text/plain"), sellerLocation);
                        RequestBody sellingPriceReq = RequestBody.create(MediaType.parse("text/plain"), sellingPrice);
                        RequestBody givenToReq = RequestBody.create(MediaType.parse("text/plain"), givenTo);
                        RequestBody givenDateReq = RequestBody.create(MediaType.parse("text/plain"), givenDate);
                        RequestBody buyerNumberReq = RequestBody.create(MediaType.parse("text/plain"), buyerNumber);
                        RequestBody buyerLocationReq = RequestBody.create(MediaType.parse("text/plain"), buyerLocation);
                        RequestBody withPartnershipReq = RequestBody.create(MediaType.parse("text/plain"), withPartnership);
                        RequestBody mutationReq = RequestBody.create(MediaType.parse("text/plain"), mutation);

                        if (from.equals("edit")) {
                            updateBirdInfo(ringNumber, image, ringNumberReq, sexReq, hatchDateReq, arrivalDateReq, approxAgeReq, sizeReq, colorReq, crestedReq, fatherReq, motherReq,
                                    statusReq, cageNumberReq, ringOwnerNameReq, purchasedPriceReq, takenFromReq, takenDateReq, sellerNumberReq, sellerLocationReq,
                                    sellingPriceReq, givenToReq, givenDateReq, buyerNumberReq, buyerLocationReq, withPartnershipReq, mutationReq);
                        } else {

                            uploadBirdInfo(image, ringNumberReq, sexReq, hatchDateReq, arrivalDateReq, approxAgeReq, sizeReq, colorReq, crestedReq, fatherReq, motherReq,
                                    statusReq, cageNumberReq, ringOwnerNameReq, purchasedPriceReq, takenFromReq, takenDateReq, sellerNumberReq, sellerLocationReq,
                                    sellingPriceReq, givenToReq, givenDateReq, buyerNumberReq, buyerLocationReq, withPartnershipReq, mutationReq);
                        }
                    }
                } else {
                    Toast.makeText(AddBirdActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        sellerLocationText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start the autocomplete intent.
//                Intent intent = new Autocomplete.IntentBuilder(
//                        AutocompleteActivityMode.OVERLAY, fields)
//                        .setCountry("PK")
//                        .build(AddBirdActivity.this);
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//            }
//        });
    }

    private void init() {
        sexSpinner = findViewById(R.id.sexSpinner);
        sexAdapter = ArrayAdapter.createFromResource(AddBirdActivity.this, R.array.sex, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(sexAdapter);

        sizeSpinner = findViewById(R.id.sizeSpinner);
        sizeAdapter = ArrayAdapter.createFromResource(AddBirdActivity.this, R.array.size, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        crestedSpinner = findViewById(R.id.crestedSpinner);
        crestedAdapter = ArrayAdapter.createFromResource(AddBirdActivity.this, R.array.crested, android.R.layout.simple_spinner_item);
        crestedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crestedSpinner.setAdapter(crestedAdapter);

        //init input layout views
        ringNumberInputLayout = findViewById(R.id.ringNumberInputLayout);
        hatchDateInputLayout = findViewById(R.id.hatchDateInputLayout);
        arrivalDateInputLayout = findViewById(R.id.arrivalDateInputLayout);
        approxAgeInputLayout = findViewById(R.id.approxAgeInputLayout);
        colorInputLayout = findViewById(R.id.colorInputLayout);
        motherInputLayout = findViewById(R.id.motherInputLayout);
        fatherInputLayout = findViewById(R.id.fatherInputLayout);
        statusInputLayout = findViewById(R.id.statusInputLayout);
        cageNumberInputLayout = findViewById(R.id.cageNumberInputLayout);
        ringOwnerNameInputLayout = findViewById(R.id.ringOwnerNameInputLayout);
        purchasedPriceInputLayout = findViewById(R.id.purchasedPriceInputLayout);
        takenFromInputLayout = findViewById(R.id.takenFromInputLayout);
        takenDateInputLayout = findViewById(R.id.takenDateInputLayout);
        sellerNumberInputLayout = findViewById(R.id.sellerNumberInputLayout);
        sellerLocationInputLayout = findViewById(R.id.sellerLocationInputLayout);
        sellingPriceInputLayout = findViewById(R.id.sellingPriceInputLayout);
        givenToInputLayout = findViewById(R.id.givenToInputLayout);
        givenDateInputLayout = findViewById(R.id.givenDateInputLayout);
        buyerNumberInputLayout = findViewById(R.id.buyerNumberInputLayout);
        buyerLocationInputLayout = findViewById(R.id.buyerLocationInputLayout);
        withPartnershipInputLayout = findViewById(R.id.withPartnershipInputLayout);
        mutationInputLayout = findViewById(R.id.mutationInputLayout);

        //init edit text views
        ringNumberText = findViewById(R.id.ringNumberText);
        hatchDateText = findViewById(R.id.hatchDateText);
        arrivalDateText = findViewById(R.id.arrivalDateText);
        approxAgeText = findViewById(R.id.approxAgeText);
        colorText = findViewById(R.id.colorText);
        motherText = findViewById(R.id.motherText);
        fatherText = findViewById(R.id.fatherText);
        statusText = findViewById(R.id.statusText);
        cageNumberText = findViewById(R.id.cageNumberText);
        ringOwnerNameText = findViewById(R.id.ringOwnerNameText);
        purchasedPriceText = findViewById(R.id.purchasedPriceText);
        takenFromText = findViewById(R.id.takenFromText);
        takenDateText = findViewById(R.id.takenDateText);
        sellerNumberText = findViewById(R.id.sellerNumberText);
        sellerLocationText = findViewById(R.id.sellerLocationText);
        sellingPriceText = findViewById(R.id.sellingPriceText);
        givenToText = findViewById(R.id.givenToText);
        givenDateText = findViewById(R.id.givenDateText);
        buyerNumberText = findViewById(R.id.buyerNumberText);
        buyerLocationText = findViewById(R.id.buyerLocationText);
        withPartnershipText = findViewById(R.id.withPartnershipText);
        mutationText = findViewById(R.id.mutationText);

        //init image view views
        mutationBtn = findViewById(R.id.mutationListBtn);
        bird_profile_image = (CircleImageView) findViewById(R.id.bird_profile_image);

        //init button views
        submitBtn = findViewById(R.id.submitBtn);

        scrollView = findViewById(R.id.scrollView);

        title = findViewById(R.id.title);

        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);

        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

    }


    private boolean isRingNumberValid(String ringNumber) {
        if (ringNumber.isEmpty()) {
            isRingNumValid = false;
            ringNumberInputLayout.setError("Ring Number must not be empty");
            return false;
        } else {
            isRingNumValid = true;
            ringNumberInputLayout.setError(null);
            return true;
        }
    }

    private boolean isRingOwnerNameValid(String ringOwnerName) {
        if (ringOwnerName.isEmpty()) {
            isRingOwnerNameValid = false;
            ringOwnerNameInputLayout.setError("Ring Owner Name must not be empty");
            return false;
        } else {
            isRingOwnerNameValid = true;
            ringOwnerNameInputLayout.setError(null);
            return true;
        }
    }

    private void updateBirdInfo(String ring_no, MultipartBody.Part image, RequestBody ringNum, RequestBody sex,
                                RequestBody hatchDate, RequestBody arrivalDate, RequestBody approxAge, RequestBody size,
                                RequestBody color, RequestBody crested, RequestBody father, RequestBody mother,
                                RequestBody status, RequestBody cageNumber, RequestBody ringOwnerName, RequestBody purchasedPrice,
                                RequestBody takenFrom, RequestBody takenDate, RequestBody sellerNumber, RequestBody sellerLocation,
                                RequestBody sellingPrice, RequestBody givenTo, RequestBody givenDate, RequestBody buyerNumber,
                                RequestBody buyerLocation, RequestBody withPartnership, RequestBody mutation) {
        loadingDialog.showLoading();
        //update api call
        apiCall.updateBirdInfo("Token " + SharedPreferencesHandler.getToken(), ring_no, image, ringNum, sex, hatchDate, arrivalDate, approxAge, size, color, crested, father, mother, status, cageNumber,
                ringOwnerName, purchasedPrice, takenFrom, takenDate, sellerNumber, sellerLocation, sellingPrice, givenTo, givenDate,
                buyerNumber, buyerLocation, withPartnership, mutation).enqueue(new Callback<BirdModel>() {
            @Override
            public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            loadingDialog.dismiss();
                            scrollView.fullScroll(View.FOCUS_UP);
                            ringNumberText.setText("");
                            sexSpinner.setSelection(0);
                            hatchDateText.setText("");
                            arrivalDateText.setText("");
                            approxAgeText.setText("");
                            sizeSpinner.setSelection(0);
                            colorText.setText("");
                            crestedSpinner.setSelection(0);
                            motherText.setText("");
                            fatherText.setText("");
                            statusText.setText("");
                            cageNumberText.setText("");
                            ringOwnerNameText.setText("");
                            purchasedPriceText.setText("");
                            takenFromText.setText("");
                            takenDateText.setText("");
                            sellerNumberText.setText("");
                            sellerLocationText.setText("");
                            sellingPriceText.setText("");
                            givenToText.setText("");
                            givenDateText.setText("");
                            buyerNumberText.setText("");
                            buyerLocationText.setText("");
                            withPartnershipText.setText("");
                            mutationText.setText("");
                            bird_profile_image.setImageDrawable(getDrawable(R.drawable.bird));
                            finish();
                            Log.d("Response", "Updated successfully");
                            Toast.makeText(AddBirdActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingDialog.dismiss();
                            Log.d("Response", "Update failed");
                            Toast.makeText(AddBirdActivity.this, "Updated failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingDialog.dismiss();
                    Log.d("Response", response.message());
                    Toast.makeText(AddBirdActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BirdModel> call, Throwable t) {
                loadingDialog.dismiss();
                Log.d("Response", t.getMessage());
                Toast.makeText(AddBirdActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChooser() {
        AlertDialog.Builder uploadDialog = new AlertDialog.Builder(AddBirdActivity.this)
                .setItems(new String[]{"Upload Image", "Take Image"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //Upload image
                            askForGalleryPermission();
                        } else if (which == 1) {
                            //Take Image
                            askCameraPermission();
                        }
                    }
                });
        AlertDialog dialog = uploadDialog.create();
        dialog.show();
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(AddBirdActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED |
                ContextCompat.checkSelfPermission(AddBirdActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddBirdActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void askForGalleryPermission() {
        if (ContextCompat.checkSelfPermission(AddBirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddBirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddBirdActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Denied permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You have to allow permission from settings", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddBirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Denied permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You have to allow permission from settings", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openGallery() {
        Intent uploadImageIntent = new Intent(Intent.ACTION_PICK);
        uploadImageIntent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        uploadImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(uploadImageIntent, GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeImageIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("Camera", ex.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(AddBirdActivity.this,
                        "com.koders.android.fileprovider",
                        photoFile);
                takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeImageIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = AddBirdActivity.this.getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    Glide.with(this).load(picturePath).into(bird_profile_image);
                }
                break;


            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Glide.with(this).load(picturePath).into(bird_profile_image);
                }
                break;

            case AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    sellerLocationText.setText(place.getName() + "," + place.getAddress());
                    Log.d("Places", place.getName());
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("Places", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
        }
    }

    private void getBird(String ringNum) {
        loadingDialog.showLoading();
        apiCall.getBird(ringNum).enqueue(new Callback<BirdModel>() {
            @Override
            public void onResponse(Call<BirdModel> call, Response<BirdModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            BirdInfo birdInfo = response.body().getBirdInfo();
                            loadingDialog.dismiss();
                            Log.d("Response", "true");

                            if (birdInfo != null) {
                                Picasso.get().load(Constants.BASE_URL + birdInfo.getImage()).placeholder(R.drawable.bird).into(bird_profile_image);
                                ringNumberText.setText(birdInfo.getRingNumber());
                                sexSpinner.setSelection(sexAdapter.getPosition(birdInfo.getSex()));
                                hatchDateText.setText(birdInfo.getHatchDate());
                                arrivalDateText.setText(birdInfo.getArrivalDate());
                                approxAgeText.setText(birdInfo.getApproxAge());
                                sizeSpinner.setSelection(sizeAdapter.getPosition(birdInfo.getSize()));
                                colorText.setText(birdInfo.getColor());
                                crestedSpinner.setSelection(crestedAdapter.getPosition(birdInfo.getCrested()));
                                motherText.setText(birdInfo.getMother());
                                fatherText.setText(birdInfo.getFather());
                                statusText.setText(birdInfo.getStatus());
                                cageNumberText.setText(birdInfo.getCageNumber());
                                ringOwnerNameText.setText(birdInfo.getRingOwnerName());
                                purchasedPriceText.setText(birdInfo.getPurchasedPrice());
                                takenFromText.setText(birdInfo.getTakenFrom());
                                takenDateText.setText(birdInfo.getTakenDate());
                                sellerNumberText.setText(birdInfo.getSellerNumber());
                                sellerLocationText.setText(birdInfo.getSellerLocation());
                                sellingPriceText.setText(birdInfo.getSellingPrice());
                                givenToText.setText(birdInfo.getGivenTo());
                                givenDateText.setText(birdInfo.getGivenDate());
                                buyerNumberText.setText(birdInfo.getBuyerNumber());
                                buyerLocationText.setText(birdInfo.getBuyerLocation());
                                withPartnershipText.setText(birdInfo.getWithPartnership());
                                mutationText.setText(birdInfo.getMutation());
                            }
                        } else {
                            loadingDialog.dismiss();
                            Log.d("Response", "false");
                        }

                    }
                } else {
                    loadingDialog.dismiss();
                    Log.d("Response", response.message());
                }
            }

            @Override
            public void onFailure(Call<BirdModel> call, Throwable t) {
                loadingDialog.dismiss();
                Log.d("Response", t.getMessage());
            }
        });
    }

    private void uploadBirdInfo(MultipartBody.Part image, RequestBody ringNum, RequestBody sex,
                                RequestBody hatchDate, RequestBody arrivalDate, RequestBody approxAge, RequestBody size,
                                RequestBody color, RequestBody crested, RequestBody father, RequestBody mother,
                                RequestBody status, RequestBody cageNumber, RequestBody ringOwnerName, RequestBody purchasedPrice,
                                RequestBody takenFrom, RequestBody takenDate, RequestBody sellerNumber, RequestBody sellerLocation,
                                RequestBody sellingPrice, RequestBody givenTo, RequestBody givenDate, RequestBody buyerNumber,
                                RequestBody buyerLocation, RequestBody withPartnership, RequestBody mutation) {
        loadingDialog.showLoading();
        apiCall.addBirdInfo("Token " + SharedPreferencesHandler.getToken(), image, ringNum, sex, hatchDate, arrivalDate, approxAge, size, color, crested, father, mother, status, cageNumber,
                ringOwnerName, purchasedPrice, takenFrom, takenDate, sellerNumber, sellerLocation, sellingPrice, givenTo, givenDate,
                buyerNumber, buyerLocation, withPartnership, mutation).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            loadingDialog.dismiss();
                            String message = response.body().getMessage();

                            Toast.makeText(AddBirdActivity.this, message, Toast.LENGTH_SHORT).show();

                            Log.d("Response", message);

                            scrollView.fullScroll(View.FOCUS_UP);
                            ringNumberText.setText("");
                            sexSpinner.setSelection(0);
                            hatchDateText.setText("");
                            arrivalDateText.setText("");
                            approxAgeText.setText("");
                            sizeSpinner.setSelection(0);
                            colorText.setText("");
                            crestedSpinner.setSelection(0);
                            motherText.setText("");
                            fatherText.setText("");
                            statusText.setText("");
                            cageNumberText.setText("");
                            ringOwnerNameText.setText("");
                            purchasedPriceText.setText("");
                            takenFromText.setText("");
                            takenDateText.setText("");
                            sellerNumberText.setText("");
                            sellerLocationText.setText("");
                            sellingPriceText.setText("");
                            givenToText.setText("");
                            givenDateText.setText("");
                            buyerNumberText.setText("");
                            buyerLocationText.setText("");
                            withPartnershipText.setText("");
                            mutationText.setText("");
                            bird_profile_image.setImageDrawable(getDrawable(R.drawable.bird));
                        } else {
                            loadingDialog.dismiss();
                            String message = response.body().getMessage();
                            Log.d("Response", message);
                            Toast.makeText(AddBirdActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(AddBirdActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(AddBirdActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
