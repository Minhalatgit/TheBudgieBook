package com.koders.budgie.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.koders.budgie.R;
import com.koders.budgie.adapters.NavigationAdapter;
import com.koders.budgie.database.DatabaseHandler;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.model.Data;
import com.koders.budgie.model.Navigation;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharePreferencesHandler;
import com.koders.budgie.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationAdapter.onNavigationClick {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView hamburger;
    private RecyclerView homeNavigationRecyclerView;
    ApiCall apiCall;
    LoadingDialog loadingDialog;
    List<Navigation> navigationList;
    RecyclerView.LayoutManager layoutManager;
    NavigationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer);
        hamburger = findViewById(R.id.hamburger);
        navigationView = findViewById(R.id.nav_view);
        homeNavigationRecyclerView = findViewById(R.id.homeNavigationRecyclerView);
        loadingDialog = new LoadingDialog(HomeActivity.this);
        navigationList = new ArrayList<>();

        populateHomeNavigation();
        layoutManager = new GridLayoutManager(this, 2);
        adapter = new NavigationAdapter(navigationList, HomeActivity.this, this);
        homeNavigationRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        homeNavigationRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        homeNavigationRecyclerView.setHasFixedSize(true);
        homeNavigationRecyclerView.setLayoutManager(layoutManager);
        homeNavigationRecyclerView.setAdapter(adapter);


        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);
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
        apiCall.logoutUser("Token " + SharePreferencesHandler.getToken()).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            loadingDialog.dismiss();
                            String message = response.body().getMessage();

                            SharePreferencesHandler.setIsLogin(false);
                            SharePreferencesHandler.setToken("");
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
                //to open home activity
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                //open my profile screen
                break;
            case R.id.nav_about:
                //open about us screen
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
