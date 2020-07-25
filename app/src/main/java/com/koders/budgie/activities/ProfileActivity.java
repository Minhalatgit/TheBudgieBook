package com.koders.budgie.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.koders.budgie.R;
import com.koders.budgie.config.Constants;
import com.koders.budgie.model.Profile;
import com.koders.budgie.model.User;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
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

public class ProfileActivity extends AppCompatActivity {

    private EditText usernameText, emailText, firstNameText, lastNameText, countryText, tagLineText;
    private TextInputLayout userNameLayout, emailInputLayout, firstNameInputLayout, lastNameInputLayout, countryInputLayout, tagLineInputLayout;
    private Toolbar toolbar;
    private TextView toolbarText;
    private CircleImageView profileImage;
    private ImageView upload;
    private ApiCall apiCall;
    Drawable usernameDrawable, emailDrawable, firstNameDrawable, lastNameDrawable, countryDrawable, tagLineDrawable;
    MenuItem editItem, doneItem, cancelItem;
    boolean isEditMode = true;
    private static final int GALLERY_REQUEST_CODE = 10;
    private static final int CAMERA_REQUEST_CODE = 20;
    String picturePath;
    User userData;
    MultipartBody.Part image;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        //disable text fields to not get edited until edit icon is clicked
        disableTextFields();

        //Populate fields here
        populateFields();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
//                startActivity(new Intent(ProfileActivity.this, ProfilePictureActivity.class));
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
            }
        });

    }

    private void init() {
        userNameLayout = findViewById(R.id.userNameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        firstNameInputLayout = findViewById(R.id.firstNameInputLayout);
        lastNameInputLayout = findViewById(R.id.lastNameInputLayout);
        countryInputLayout = findViewById(R.id.countryInputLayout);
        tagLineInputLayout = findViewById(R.id.tagLineInputLayout);

        usernameText = findViewById(R.id.userNameText);
        emailText = findViewById(R.id.emailText);
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        countryText = findViewById(R.id.countryText);
        tagLineText = findViewById(R.id.tagLineText);
        profileImage = findViewById(R.id.profileImage);
        upload = findViewById(R.id.upload);

        toolbar = findViewById(R.id.toolbar);
        toolbarText = toolbar.findViewById(R.id.toolbarText);

        toolbarText.setText("My Profile");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);
        loadingDialog = new LoadingDialog(ProfileActivity.this);
    }

    private void updateProfile() {
        Utility.hideKeyboard(ProfileActivity.this);
        String capitalizedCountry = "";

        String username = usernameText.getText().toString().trim();
        String email = emailText.getText().toString().trim().toLowerCase();
        String firstName = firstNameText.getText().toString().trim();
        String lastName = lastNameText.getText().toString().trim();
        String country = countryText.getText().toString().trim();
        String tagLine = tagLineText.getText().toString().trim();

        if (!country.equals("")) {
            String firstLetter = country.substring(0, 1).toUpperCase();
            capitalizedCountry = firstLetter + country.substring(1);
        }

        if (!isUsernameValid(username) | !isEmailValid(email) |
                !isFirstNameValid(firstName) | !isLastNameValid(lastName) | !isValidCountry(capitalizedCountry)) {
            Log.d("TAG", "onClick: any field is empty");
        } else {
            loadingDialog.showLoading();

            if (picturePath != null) {
                File file = new File(picturePath);
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
            }

            RequestBody userNameReq = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody emailReq = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody firstNameReq = RequestBody.create(MediaType.parse("text/plain"), firstName);
            RequestBody lastNameReq = RequestBody.create(MediaType.parse("text/plain"), lastName);
            RequestBody countryReq = RequestBody.create(MediaType.parse("text/plain"), capitalizedCountry);
            RequestBody tagLineReq = RequestBody.create(MediaType.parse("text/plain"), tagLine);

            apiCall.updateProfile("Token " + SharedPreferencesHandler.getToken(), userNameReq,
                    emailReq, firstNameReq, lastNameReq, image, countryReq, tagLineReq).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {

                            User user = response.body().getUser();

                            if (response.body().getStatus()) {

                                loadingDialog.dismiss();

                                SharedPreferencesHandler.setEmail(user.getEmail());
                                SharedPreferencesHandler.setUsername(user.getUsername());
                                SharedPreferencesHandler.setFirstName(user.getFirstName());
                                SharedPreferencesHandler.setLastName(user.getLastName());
                                SharedPreferencesHandler.setCountry(user.getCountry());
                                SharedPreferencesHandler.setTagLine(user.getTagLine());
                                SharedPreferencesHandler.setImage(user.getImage());

                                populateFields();
                                isEditMode = true;
                                disableTextFields();
                                invalidateOptionsMenu();
                                Toast.makeText(ProfileActivity.this, "Update successful", Toast.LENGTH_SHORT).show();


                            } else {
                                loadingDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    loadingDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void populateFields() {
        userData = new User(SharedPreferencesHandler.getEmail(), SharedPreferencesHandler.getUsername(),
                SharedPreferencesHandler.getFirstName(), SharedPreferencesHandler.getLastName(), SharedPreferencesHandler.getTagLine(),
                SharedPreferencesHandler.getImage(), SharedPreferencesHandler.getCountry());

        usernameText.setText(userData.getUsername());
        emailText.setText(userData.getEmail());
        firstNameText.setText(userData.getFirstName());
        lastNameText.setText(userData.getLastName());
        countryText.setText(userData.getCountry());
        tagLineText.setText(userData.getTagLine());
        Glide.with(ProfileActivity.this)
                .load(Constants.BASE_URL + userData.getImage())
                .placeholder(R.drawable.profile_image)
                .into(profileImage);
    }

    private void enableTextFields() {
        usernameText.setEnabled(true);
        emailText.setEnabled(true);
        firstNameText.setEnabled(true);
        lastNameText.setEnabled(true);
        countryText.setEnabled(true);
        tagLineText.setEnabled(true);
        profileImage.setEnabled(true);
        upload.setVisibility(View.VISIBLE);

        userNameLayout.setCounterEnabled(true);

        usernameText.setBackground(usernameDrawable);
        emailText.setBackground(emailDrawable);
        firstNameText.setBackground(firstNameDrawable);
        lastNameText.setBackground(lastNameDrawable);
        countryText.setBackground(countryDrawable);
        tagLineText.setBackground(tagLineDrawable);
    }

    private void disableTextFields() {
        usernameText.setEnabled(false);
        emailText.setEnabled(false);
        firstNameText.setEnabled(false);
        lastNameText.setEnabled(false);
        countryText.setEnabled(false);
        tagLineText.setEnabled(false);
        profileImage.setEnabled(false);

        picturePath = null;
        upload.setVisibility(View.GONE);

        usernameDrawable = usernameText.getBackground();
        emailDrawable = emailText.getBackground();
        firstNameDrawable = firstNameText.getBackground();
        lastNameDrawable = lastNameText.getBackground();
        countryDrawable = countryText.getBackground();
        tagLineDrawable = tagLineText.getBackground();

        usernameText.setBackground(null);
        emailText.setBackground(null);
        firstNameText.setBackground(null);
        lastNameText.setBackground(null);
        countryText.setBackground(null);
        tagLineText.setBackground(null);

        userNameLayout.setCounterEnabled(false);

        userNameLayout.setError(null);
        emailInputLayout.setError(null);
        firstNameInputLayout.setError(null);
        lastNameInputLayout.setError(null);
        countryInputLayout.setError(null);

    }

    private void showChooser() {
        AlertDialog.Builder uploadDialog = new AlertDialog.Builder(ProfileActivity.this)
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

    private void askForGalleryPermission() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED |
                ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        } else {
            openCamera();

        }
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
                Uri photoURI = FileProvider.getUriForFile(ProfileActivity.this,
                        "com.koders.android.fileprovider",
                        photoFile);
                takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeImageIntent, CAMERA_REQUEST_CODE);
            }
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

    private void openGallery() {
        Intent uploadImageIntent = new Intent(Intent.ACTION_PICK);
        uploadImageIntent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        uploadImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(uploadImageIntent, GALLERY_REQUEST_CODE);
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
                    Glide.with(this).load(picturePath).into(profileImage);
                }
                break;

            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Glide.with(this).load(picturePath).into(profileImage);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Denied permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You have to allow permission from settings", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Denied permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You have to allow permission from settings", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        editItem = menu.findItem(R.id.edit);
        cancelItem = menu.findItem(R.id.cancel);
        doneItem = menu.findItem(R.id.done);

        if (isEditMode) {
            editItem.setVisible(true);
            cancelItem.setVisible(false);
            doneItem.setVisible(false);
        } else {
            editItem.setVisible(false);
            cancelItem.setVisible(true);
            doneItem.setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.edit:
                isEditMode = false;
                enableTextFields();
                invalidateOptionsMenu();
                break;

            case R.id.cancel:
                populateFields();
                isEditMode = true;
                disableTextFields();
                invalidateOptionsMenu();
                break;

            case R.id.done:
                if (Utility.isNetworkConnected()) {
                    updateProfile();
                } else {
                    Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                }

                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
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