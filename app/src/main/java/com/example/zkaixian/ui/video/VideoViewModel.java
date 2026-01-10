package com.example.zkaixian.ui.video;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.pojo.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoViewModel extends ViewModel {
    private final MutableLiveData<List<Video>> videoList = new MutableLiveData<>();

    public LiveData<List<Video>> getVideoList() {
        return videoList;
    }

    public void fetchVideoList() {
        RetrofitClient.getApiService().getVideoList().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("VideoViewModel", "视频列表请求成功" + response.body());
                    videoList.setValue(response.body());
                } else {
                    Log.e("VideoViewModel", "视频列表请求失败: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {
                Log.e("VideoViewModel", "视频列表网络错误: " + t.getMessage());
            }
        });
    }
}