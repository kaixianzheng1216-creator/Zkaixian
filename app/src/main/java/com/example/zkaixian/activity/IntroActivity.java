package com.example.zkaixian.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zkaixian.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int backgroundColor = R.color.white;
        int titleColor = R.color.black;
        int descColor = R.color.black;

        addSlide(AppIntroFragment.createInstance("第一日", "启动页、引导页、闪屏页与基础框架搭建", R.drawable.day1, backgroundColor, titleColor, descColor));
        addSlide(AppIntroFragment.createInstance("第二日", "LiveData + Retrofit 网络请求与下拉刷新", R.drawable.day2, backgroundColor, titleColor, descColor));
        addSlide(AppIntroFragment.createInstance("第三日", "RecyclerAdapter 框架与轮播图 Banner", R.drawable.day3, backgroundColor, titleColor, descColor));
        addSlide(AppIntroFragment.createInstance("第四日", "新闻详情页 AgentWeb 与 Python 详情页", R.drawable.day4, backgroundColor, titleColor, descColor));
        addSlide(AppIntroFragment.createInstance("第五日", "爆炸菜单 BoomMenu 与统计图表", R.drawable.day5, backgroundColor, titleColor, descColor));
        addSlide(AppIntroFragment.createInstance("第六日", "视频列表与视频播放器 GSYVideoPlayer", R.drawable.day6, backgroundColor, titleColor, descColor));
        addSlide(AppIntroFragment.createInstance("第七日", "我的界面与基于 Bmob 的登录注册", R.drawable.day7, backgroundColor, titleColor, descColor));
        addSlide(AppIntroFragment.createInstance("第八日", "百度地图 API", R.drawable.day8, backgroundColor, titleColor, descColor));

        setSkipText("跳过");
        setDoneText("完成");

        setColorSkipButton(Color.BLACK);
        setNextArrowColor(Color.BLACK);
        setIndicatorColor(Color.BLACK, Color.GRAY);
        setColorDoneText(Color.BLACK);
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        completeIntro();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        completeIntro();
    }

    private void completeIntro() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isFirstRun", false).apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}