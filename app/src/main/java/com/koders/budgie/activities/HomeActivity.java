package com.koders.budgie.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.koders.budgie.R;
import com.koders.budgie.adapters.NavigationAdapter;
import com.koders.budgie.config.Constants;
import com.koders.budgie.model.Profile;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.model.Data;
import com.koders.budgie.model.Navigation;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharedPreferencesHandler;
import com.koders.budgie.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationAdapter.onNavigationClick {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    //    private ImageView hamburger;
    private RecyclerView homeNavigationRecyclerView;
    private CircleImageView profileDisplay;

    ApiCall apiCall;
    LoadingDialog loadingDialog;
    List<Navigation> navigationList;
    RecyclerView.LayoutManager layoutManager;
    NavigationAdapter adapter;
    Toolbar toolbar;
    TextView toolbarText, userFullName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

//        hamburger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(HomeActivity.this)
                .load(Constants.BASE_URL + SharedPreferencesHandler.getImage())
                .placeholder(R.drawable.profile_image)
                .into(profileDisplay);
        userFullName.setText(SharedPreferencesHandler.getFirstName() + " " + SharedPreferencesHandler.getLastName());
        userEmail.setText(SharedPreferencesHandler.getEmail());
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer);
        //hamburger = findViewById(R.id.hamburger);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        profileDisplay = headerView.findViewById(R.id.profileDisplay);
        userFullName = headerView.findViewById(R.id.profileName);
        userEmail = headerView.findViewById(R.id.profileEmail);
        homeNavigationRecyclerView = findViewById(R.id.homeNavigationRecyclerView);
        toolbar = findViewById(R.id.toolbar);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        loadingDialog = new LoadingDialog(HomeActivity.this);
        navigationList = new ArrayList<>();

        toolbarText.setText("Home");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        populateHomeNavigation();
        layoutManager = new GridLayoutManager(this, 2);
        adapter = new NavigationAdapter(navigationList, HomeActivity.this, this);
        homeNavigationRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        homeNavigationRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        homeNavigationRecyclerView.setHasFixedSize(true);
        homeNavigationRecyclerView.setLayoutManager(layoutManager);
        homeNavigationRecyclerView.setAdapter(adapter);

        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);

        Glide.with(HomeActivity.this)
                .load(Constants.BASE_URL + SharedPreferencesHandler.getImage())
                .placeholder(R.drawable.profile_image)
                .into(profileDisplay);
        userFullName.setText(SharedPreferencesHandler.getFirstName() + " " + SharedPreferencesHandler.getLastName());
        userEmail.setText(SharedPreferencesHandler.getEmail());
    }

    private void populateHomeNavigation() {
        navigationList.add(new Navigation("My Birds", R.drawable.bird));
        navigationList.add(new Navigation("My Cards", R.drawable.bird));
        navigationList.add(new Navigation("My Aviary", R.drawable.bird));
        navigationList.add(new Navigation("Bird Of The Month", R.drawable.bird));
        navigationList.add(new Navigation("Genetics", R.drawable.bird));
        navigationList.add(new Navigation("For Sale", R.drawable.bird));
        navigationList.add(new Navigation("Genetics", R.drawable.bird));
        navigationList.add(new Navigation("For Sale", R.drawable.bird));
        navigationList.add(new Navigation("Genetics", R.drawable.bird));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logoutUser() {
        loadingDialog.showLoading();
        apiCall.logoutUser("Token " + SharedPreferencesHandler.getToken()).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            loadingDialog.dismiss();
                            String message = response.body().getMessage();

                            SharedPreferencesHandler.setIsLogin(false);
                            SharedPreferencesHandler.setToken("");
                            SharedPreferencesHandler.setEmail("");
                            SharedPreferencesHandler.setUsername("");
                            SharedPreferencesHandler.setFirstName("");
                            SharedPreferencesHandler.setLastName("");
                            SharedPreferencesHandler.setCountry("");
                            SharedPreferencesHandler.setTagLine("");
                            SharedPreferencesHandler.setImage("");

                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(HomeActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(HomeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                break;
            case R.id.rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Budgie Book");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Making birds' sale purchase quite easier for you!\nKindly have a look.");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.logout:
                if (Utility.isNetworkConnected()) {
                    logoutUser();
                } else {
                    Toast.makeText(HomeActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(HomeActivity.this, BirdsActivity.class));
                break;
            case 1:
                //Move to some other activity
//                startActivity(new Intent(HomeActivity.this, BirdsActivity.class));
                break;
            case 2:
                //Move to some other activity
//                startActivity(new Intent(HomeActivity.this, BirdsActivity.class));
                break;
            case 3:
                //Move to some other activity
//                startActivity(new Intent(HomeActivity.this, BirdsActivity.class));
                break;
        }
    }
}
