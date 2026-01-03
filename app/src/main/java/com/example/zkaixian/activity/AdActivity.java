package com.example.zkaixian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.zkaixian.R;

public class AdActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private String adImageUrl = "https://olaholah-1326678836.cos.ap-guangzhou.myqcloud.com/android/ad.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        Button btnSkip = findViewById(R.id.btn_skip);
        ImageView ivAd = findViewById(R.id.iv_ad);

        Glide.with(this)
                .asBitmap()
                .load(adImageUrl)
                .into(ivAd);

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSkip.setText("跳过 " + (millisUntilFinished / 1000 + 1) + "s");
            }

            @Override
            public void onFinish() {
                startMainActivity();
            }
        };

        countDownTimer.start();

        btnSkip.setOnClickListener(v -> startMainActivity());
    }

    private void startMainActivity() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("is_from_ad", true);

        startActivity(intent);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}