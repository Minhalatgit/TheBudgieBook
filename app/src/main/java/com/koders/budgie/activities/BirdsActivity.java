package com.koders.budgie.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koders.budgie.R;
import com.koders.budgie.adapters.BirdListAdapter;
import com.koders.budgie.database.DatabaseHandler;
import com.koders.budgie.networkcalls.ApiCall;
import com.koders.budgie.networkcalls.RetrofitClient;
import com.koders.budgie.model.BirdInfo;
import com.koders.budgie.model.BirdResponse;
import com.koders.budgie.pagination.PaginationScrollListener;
import com.koders.budgie.utils.LoadingDialog;
import com.koders.budgie.utils.SharePreferencesHandler;
import com.koders.budgie.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BirdsActivity extends AppCompatActivity {

    private RecyclerView birdsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    ApiCall apiCall;
    List<BirdInfo> birdInfoList = new ArrayList<>();
    BirdListAdapter birdListAdapter;
    LinearLayoutManager layoutManager;
    LoadingDialog loadingDialog;
    DatabaseHandler databaseHandler;
    BirdListAdapter.OnBirdClickListener listener;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birds);
        init();

        if (Utility.isNetworkConnected()) {
            getAllBirdsInfo();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isNetworkConnected()) {
                    currentPage = PAGE_START;
                    isLastPage = false;
                    isLoading = false;
                    birdListAdapter.clear();
                    birdListAdapter.notifyDataSetChanged();
                    getAllBirdsInfo();

                } else {
                    Toast.makeText(BirdsActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        currentPage = PAGE_START;
        isLastPage = false;
        isLoading = false;
        birdListAdapter.clear();
        birdListAdapter.notifyDataSetChanged();
        getAllBirdsInfo();
        Log.d("OnRestart", "Done");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("OnResume", "Done");
    }

    private void init() {
        listener = new BirdListAdapter.OnBirdClickListener() {
            @Override
            public void onBirdClick(String ringNum) {
                Log.d("OnClick", ringNum);
                Intent intent = new Intent(BirdsActivity.this, BirdDetailActivity.class);
                intent.putExtra("from", "add");
                intent.putExtra("ringNum", ringNum);
                startActivity(intent);
            }
        };

        databaseHandler = new DatabaseHandler(BirdsActivity.this);
        birdsRecyclerView = findViewById(R.id.birdsRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        toolbar = findViewById(R.id.toolbar);
        loadingDialog = new LoadingDialog(BirdsActivity.this);
        apiCall = RetrofitClient.getRetrofit().create(ApiCall.class);
        layoutManager = new LinearLayoutManager(this);
        birdsRecyclerView.setLayoutManager(layoutManager);

        birdListAdapter = new BirdListAdapter(BirdsActivity.this, birdInfoList, listener);
        birdsRecyclerView.setAdapter(birdListAdapter);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        birdsRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                //page is scrolled to end now load new items
                isLoading = true;
                currentPage += 1;
                getBirdsInfoPaginated();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void getAllBirdsInfo() {
        loadingDialog.showLoading();
        apiCall.getAllBirds("Token " + SharePreferencesHandler.getToken(), currentPage).enqueue(new Callback<BirdResponse>() {
            @Override
            public void onResponse(Call<BirdResponse> call, Response<BirdResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        loadingDialog.dismiss();

                        int count = response.body().getTotalBirds();
                        if (count % 10 == 0) {
                            TOTAL_PAGES = count / 10;
                        } else {
                            TOTAL_PAGES = (count / 10) + 1;
                        }
                        birdListAdapter.addAll(response.body().getBirdInfoList());
                        if (currentPage < TOTAL_PAGES) birdListAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                } else {
                    loadingDialog.dismiss();
                    Log.d("Response", response.message());
                }
            }

            @Override
            public void onFailure(Call<BirdResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Log.d("Response", t.getMessage());
            }
        });
    }

    private void getBirdsInfoPaginated() {
        apiCall.getAllBirds("Token " + SharePreferencesHandler.getToken(), currentPage).enqueue(new Callback<BirdResponse>() {
            @Override
            public void onResponse(Call<BirdResponse> call, Response<BirdResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        birdListAdapter.removeLoadingFooter();
                        isLoading = false;

                        birdListAdapter.addAll(response.body().getBirdInfoList());
                        if (currentPage < TOTAL_PAGES) birdListAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                } else {
                    Log.d("Response", response.message());
                    birdListAdapter.removeLoadingFooter();
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<BirdResponse> call, Throwable t) {
                Log.d("Response", t.getMessage());
                birdListAdapter.removeLoadingFooter();
                isLoading = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                startActivity(new Intent(BirdsActivity.this, AddBirdActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
