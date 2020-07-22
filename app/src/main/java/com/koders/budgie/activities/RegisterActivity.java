package com.koders.budgie.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.koders.budgie.R;
import com.koders.budgie.model.User;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.model.Data;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharedPreferencesHandler;
import com.koders.budgie.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 10;
    private static final int CAMERA_REQUEST_CODE = 20;
    private EditText usernameText, emailText, passwordText, password2Text, firstNameText, lastNameText, countryText, tagLineText;
    private Button register;
    private TextInputLayout userNameLayout, emailInputLayout, passwordInputLayout, password2InputLayout, firstNameInputLayout, lastNameInputLayout, countryInputLayout, tagLineInputLayout;
    private CircleImageView registerImage;
    private static final String TAG = "RegisterActivity";
    ApiCall apiCall;
    LoadingDialog loadingDialog;
    String capitalizedCountry = "", picturePath;
    MultipartBody.Part image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.hideKeyboard(RegisterActivity.this);

                String username = usernameText.getText().toString().trim();
                String email = emailText.getText().toString().trim().toLowerCase();
                String password = passwordText.getText().toString().trim();
                String password2 = password2Text.getText().toString().trim();
                String firstName = firstNameText.getText().toString().trim();
                String lastName = lastNameText.getText().toString().trim();
                String country = countryText.getText().toString().trim();
                String tagLine = tagLineText.getText().toString().trim();

                if (!country.equals("")) {
                    String firstLetter = country.substring(0, 1).toUpperCase();
                    capitalizedCountry = firstLetter + country.substring(1);
                }

                if (!isUsernameValid(username) | !isEmailValid(email) | !isPasswordValid(password, password2) |
                        !isFirstNameValid(firstName) | !isLastNameValid(lastName) | !isValidCountry(capitalizedCountry)) {
                    Log.d(TAG, "onClick: any field is empty");
                } else {

                    if (picturePath != null) {
                        File file = new File(picturePath);
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                        image = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
                    }

                    RequestBody userNameReq = RequestBody.create(MediaType.parse("text/plain"), username);
                    RequestBody emailReq = RequestBody.create(MediaType.parse("text/plain"), email);
                    RequestBody passwordReq = RequestBody.create(MediaType.parse("text/plain"), password);
                    RequestBody password2Req = RequestBody.create(MediaType.parse("text/plain"), password2);
                    RequestBody firstNameReq = RequestBody.create(MediaType.parse("text/plain"), firstName);
                    RequestBody lastNameReq = RequestBody.create(MediaType.parse("text/plain"), lastName);
                    RequestBody countryReq = RequestBody.create(MediaType.parse("text/plain"), capitalizedCountry);
                    RequestBody tagLineReq = RequestBody.create(MediaType.parse("text/plain"), tagLine);

                    if (Utility.isNetworkConnected()) {
                        registerUser(userNameReq, emailReq, passwordReq, password2Req, firstNameReq, lastNameReq, image, countryReq, tagLineReq);
                    } else {
                        Toast.makeText(RegisterActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
            }
        });
    }

    private void init() {

        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);

        usernameText = findViewById(R.id.userNameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        password2Text = findViewById(R.id.password2Text);
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        countryText = findViewById(R.id.countryText);
        tagLineText = findViewById(R.id.tagLineText);
        register = findViewById(R.id.registerButton);
        userNameLayout = findViewById(R.id.userNameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        password2InputLayout = findViewById(R.id.password2InputLayout);
        firstNameInputLayout = findViewById(R.id.firstNameInputLayout);
        lastNameInputLayout = findViewById(R.id.lastNameInputLayout);
        countryInputLayout = findViewById(R.id.countryInputLayout);
        tagLineInputLayout = findViewById(R.id.tagLineInputLayout);
        registerImage = findViewById(R.id.profileImage);

        loadingDialog = new LoadingDialog(RegisterActivity.this);
    }

    private void registerUser(RequestBody username, RequestBody email, RequestBody password, RequestBody password2, RequestBody firstName,
                              RequestBody lastName, MultipartBody.Part image, RequestBody country, RequestBody tagLine) {
        loadingDialog.showLoading();
        apiCall.registerUser(username, email, password, password2, firstName, lastName, image, country, tagLine).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        User user = response.body().getUser();

                        if (response.body().isStatus()) {
                            loadingDialog.dismiss();

                            String message = response.body().getMessage();
                            String token = response.body().getToken();
                            boolean isMailSent = response.body().isMail_sent();

//                            SharedPreferencesHandler.setEmail(user.getEmail());
//                            SharedPreferencesHandler.setUsername(user.getUsername());
//                            SharedPreferencesHandler.setFirstName(user.getFirstName());
//                            SharedPreferencesHandler.setLastName(user.getLastName());
//                            SharedPreferencesHandler.setCountry(user.getCountry());
//                            SharedPreferencesHandler.setImage(user.getImage());
//                            SharedPreferencesHandler.setTagLine(user.getTagLine());

                            Log.d("Response", "Message: " + message + "Token: " + token);
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

//                            SharedPreferencesHandler.setIsLogin(true);
                            SharedPreferencesHandler.setToken(token);
//                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            startActivity(new Intent(RegisterActivity.this, VerifyCodeActivity.class));
                            //finishAffinity();

                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                loadingDialog.dismiss();
                Log.d("Response", t.getMessage());
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChooser() {
        AlertDialog.Builder uploadDialog = new AlertDialog.Builder(RegisterActivity.this)
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
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED |
                ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        } else {
            openCamera();

        }
    }

    private void askForGalleryPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Denied permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You have to allow permission from settings", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
                Uri photoURI = FileProvider.getUriForFile(RegisterActivity.this,
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
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            picturePath = cursor.getString(columnIndex);
                            cursor.close();
                        }
                    }
                    Glide.with(this).load(picturePath).into(registerImage);
                }
                break;

            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Glide.with(this).load(picturePath).into(registerImage);
                }
                break;
        }
    }

    private boolean isUsernameValid(String username) {
        if (username.isEmpty()) {
            userNameLayout.setError("Username must not be empty");
            return false;
        } else {
            userNameLayout.setError(null);
            return true;
        }
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

    private boolean isPasswordValid(String password, String password2) {
        boolean valid = true;
        String errorMessage = "";

        if (password.isEmpty()) {
            errorMessage = "Password must not be empty";
            valid = false;
        } else if (password2.isEmpty()) {
            errorMessage = "Password must not be empty";
            valid = false;
        } else if (!password.equals(password2)) {
            errorMessage = "Password must be same";
            valid = false;
        } else if (password.length() < 6) {
            errorMessage = "Password must contain at least 6 characters";
            valid = false;
        } else if (password2.length() < 6) {
            errorMessage = "Password must contain at least 6 characters";
            valid = false;
        } else {
            errorMessage = null;
            valid = true;
        }
        passwordInputLayout.setError(errorMessage);
        password2InputLayout.setError(errorMessage);
        return valid;
    }

    private boolean isFirstNameValid(String firstName) {
        if (firstName.isEmpty()) {
            firstNameInputLayout.setError("First Name must not be empty");
            return false;
        } else {
            firstNameInputLayout.setError(null);
            return true;
        }
    }

    private boolean isLastNameValid(String lastName) {
        if (lastName.isEmpty()) {
            lastNameInputLayout.setError("Last Name must not be empty");
            return false;
        } else {
            lastNameInputLayout.setError(null);
            return true;
        }
    }

    private boolean isValidCountry(String country) {
        if (country.isEmpty()) {
            countryInputLayout.setError("Country must not be empty");
            return false;
        } else {
            countryInputLayout.setError(null);
            return true;
        }
    }
}
