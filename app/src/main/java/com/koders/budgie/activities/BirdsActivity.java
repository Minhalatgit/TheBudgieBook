package com.koders.budgie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.koders.budgie.utils.SharedPreferencesHandler;
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
    private TextView toolbarText;
    private TextView noBirds;
    ApiCall apiCall;
    List<BirdInfo> birdInfoList = new ArrayList<>();
    BirdListAdapter birdListAdapter;
    LinearLayoutManager layoutManager;
    LoadingDialog loadingDialog;
    DatabaseHandler databaseHandler;
    BirdListAdapter.OnBirdClickListener listener;

    private boolean isLoading = true;
    private boolean isLastPage = false;
    private static final int PAGE_START = 0;
    private int start = PAGE_START;
    private int end = start + 10;

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
                    start = PAGE_START;
                    end = start + 10;
                    isLastPage = false;
                    isLoading = true;
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
        start = PAGE_START;
        end = start + 10;
        isLastPage = false;
        isLoading = true;
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
        noBirds = findViewById(R.id.noBirds);
        toolbarText = toolbar.findViewById(R.id.toolbarText);

        birdListAdapter = new BirdListAdapter(BirdsActivity.this, birdInfoList, listener);
        birdsRecyclerView.setAdapter(birdListAdapter);

        toolbarText.setText("Birds");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        birdsRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                start = end + 1;
                end = start + 9;

                birdListAdapter.addLoadingFooter();

                getBirdsInfoPaginated();
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
        apiCall.getAllBirds("Token " + SharedPreferencesHandler.getToken(), start, end).enqueue(new Callback<BirdResponse>() {
            @Override
            public void onResponse(Call<BirdResponse> call, Response<BirdResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            noBirds.setVisibility(View.GONE);
                            loadingDialog.dismiss();
                            birdListAdapter.addAll(response.body().getBirdInfoList());
                            isLoading = false;
//                            birdListAdapter.addLoadingFooter();
                        } else {
                            noBirds.setVisibility(View.VISIBLE);
                            isLastPage = true;
                            loadingDialog.dismiss();
                            Toast.makeText(BirdsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    isLastPage = true;
                    loadingDialog.dismiss();
                    Log.d("Response", response.message());
                }
            }

            @Override
            public void onFailure(Call<BirdResponse> call, Throwable t) {
                isLastPage = true;
                loadingDialog.dismiss();
                Log.d("Response", t.getMessage());
            }
        });
    }

    private void getBirdsInfoPaginated() {
        isLoading = true;
        apiCall.getAllBirds("Token " + SharedPreferencesHandler.getToken(), start, end).enqueue(new Callback<BirdResponse>() {
            @Override
            public void onResponse(Call<BirdResponse> call, Response<BirdResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            isLoading = false;
                            birdListAdapter.removeLoadingFooter();
                            noBirds.setVisibility(View.GONE);
                            birdListAdapter.addAll(response.body().getBirdInfoList());
                            //birdListAdapter.addLoadingFooter();
                        } else {
                            birdListAdapter.removeLoadingFooter();
                            //isLoading = false;
                            isLastPage = true;
//                            Toast toast = Toast.makeText(BirdsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                        }
                    }
                } else {
                    birdListAdapter.removeLoadingFooter();
                    isLoading = false;
                    isLastPage = true;
                    Log.d("Response", response.message());
                    Toast.makeText(BirdsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BirdResponse> call, Throwable t) {
                birdListAdapter.removeLoadingFooter();
                isLoading = false;
                isLastPage = true;
                Log.d("Response", t.getMessage());
                Toast.makeText(BirdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
            case android.R.id.home:
                onBackPressed(); //OR finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
