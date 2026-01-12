package com.example.zkaixian.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.amap.api.maps.MapsInitializer;
import com.example.zkaixian.R;
import com.example.zkaixian.databinding.ActivityMainBinding;
import com.lxj.xpopup.XPopup;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);
        
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
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_activity_fragment_nav_host);

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

        binding.mainActivityNavView.setItemIconTintList(colorStateList);
        binding.mainActivityNavView.setItemTextColor(colorStateList);
        binding.mainActivityNavView.setItemActiveIndicatorEnabled(false);

        NavigationUI.setupWithNavController(binding.mainActivityNavView, navController);
    }

    private void initBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController != null && navController.navigateUp()) {
                    return;
                }

                new XPopup.Builder(MainActivity.this)
                        .asConfirm("提示", "确定要退出程序吗？",
                                "取消", "确定",
                                null, () -> finish(), false)
                        .show();
            }
        });
    }
}
