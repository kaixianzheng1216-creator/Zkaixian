package com.example.zkaixian.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.zkaixian.R;
import com.example.zkaixian.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (checkAndHandleFirstRun()) return;

        if (checkAndHandleAd()) return;

        initBinding();

        initNavigation();

        initBackPressHandler();
    }

    private boolean checkAndHandleFirstRun() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            startActivity(new Intent(this, IntroActivity.class));
            finish();
            return true;
        }

        return false;
    }

    private boolean checkAndHandleAd() {
        boolean isFromAd = getIntent().getBooleanExtra("is_from_ad", false);

        if (!isFromAd) {
            startActivity(new Intent(this, AdActivity.class));
            finish();
            return true;
        }

        return false;
    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }

    private void initNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{}
        };

        int[] colors = new int[]{
                Color.parseColor("#007AFF"),
                Color.parseColor("#8E8E93")
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

        binding.navView.setItemIconTintList(colorStateList);
        binding.navView.setItemTextColor(colorStateList);
        binding.navView.setItemActiveIndicatorEnabled(false);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void initBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController != null && navController.navigateUp()) {
                    return;
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastBackPressTime < 2000) {
                    finish();
                } else {
                    lastBackPressTime = currentTime;
                    Snackbar.make(binding.getRoot(), "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
