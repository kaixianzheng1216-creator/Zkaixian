package com.example.zkaixian.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.pojo.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private final MutableLiveData<List<News>> adList = new MutableLiveData<>();

    public HomeViewModel() {
        fetchNews();
        fetchAds();
    }

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }

    public LiveData<List<News>> getAdList() {
        return adList;
    }

    public void fetchAds() {
        RetrofitClient.getApiService().getAdList().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(@NonNull Call<List<News>> call, @NonNull Response<List<News>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adList.setValue(response.body());
                } else {
                    Log.e("HomeViewModel", "广告请求失败: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<News>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "广告网络错误: " + t.getMessage());
            }
        });
    }

    public void fetchNews() {
        RetrofitClient.getApiService().getNewsList().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(@NonNull Call<List<News>> call, @NonNull Response<List<News>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsList.setValue(response.body());
                } else {
                    Log.e("HomeViewModel", "请求失败: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<News>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "网络错误: " + t.getMessage());
            }
        });
    }
}