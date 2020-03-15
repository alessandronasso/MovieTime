package com.lab.movietime.View.Activity.Activity.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lab.movietime.Adapter.MoviesAdapter;
import com.lab.movietime.BuildConfig;
import com.lab.movietime.Interface.ApiBuilder;
import com.lab.movietime.Interface.ApiService;
import com.lab.movietime.Model.MovieModel;
import com.lab.movietime.Model.MovieResponse;
import com.lab.movietime.R;
import com.lab.movietime.View.Activity.Activity.Fragment.HomeFragment;
import com.lab.movietime.View.Activity.Activity.Fragment.PlayingFragment;
import com.lab.movietime.View.Activity.Activity.Fragment.PopularFragment;
import com.lab.movietime.values.Values;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private int counter=0;
    public static int currentIndex = 0;
    private int currentApiVersion;

    public HomeFragment homeFragment;
    public PopularFragment popularFragment;
    public PlayingFragment playingFragment;


    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        setContentView(R.layout.activity_main);
        if(homeFragment == null){ homeFragment  =new HomeFragment();}
        loadFragment(homeFragment);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void hideNavigationBar() {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bbn_item1:
                    currentIndex = 0;
                    if(homeFragment == null) homeFragment = new HomeFragment();
                    loadFragment(homeFragment);
                    return true;
                case R.id.bbn_item2:
                    currentIndex = 1;
                    if(popularFragment == null) popularFragment = new PopularFragment();
                    loadFragment(popularFragment);
                    return true;
                case R.id.bbn_item3:
                    currentIndex = 2;
                    if(playingFragment == null) playingFragment = new PlayingFragment();
                    loadFragment(playingFragment);
                    return true;
            }
            return false;
        }
    };


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        ApiService apiService = ApiBuilder.getClient(MainActivity.this).create(ApiService.class);
        @Override
        public void onReceive(Context context, Intent intent) {
            counter+=1;
            Call<MovieResponse> call = apiService.getPopular(Values.CATEGORY[0],BuildConfig.API_KEY,Values.LANGUAGE,counter);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                    final List<MovieModel> movies = response.body().getResults();
                    final RecyclerView recyclerView = findViewById(R.id.rc_view);
                    if (recyclerView !=null){
                        recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.content_main, getApplicationContext()));
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse>call, Throwable t) { }
            });
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lab.movietime.Service");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lab.movietime.Service");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    public boolean isNetworkAvailable() {
        try{
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if(manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() { }
}