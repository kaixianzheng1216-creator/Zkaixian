package com.example.zkaixian.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.zkaixian.R;
import com.example.zkaixian.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        if (checkAndHandleFirstRun()) {
            return;
        }

        if (checkAndHandleAd()) {
            return;
        }

        initBinding();

        initNavigation();

        initBackPressHandler();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_chart,
                R.id.navigation_video,
                R.id.navigation_me
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void initBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                boolean back = navController.popBackStack();

                if (back) {
                    return;
                }

                long currentTime = System.currentTimeMillis();

                if (currentTime - lastBackPressTime < 2000) {
                    finish();
                } else {
                    lastBackPressTime = currentTime;

                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}