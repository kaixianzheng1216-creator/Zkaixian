package com.example.zkaixian.ui.me;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.common.ApiResponse;
import com.example.zkaixian.pojo.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeViewModel extends ViewModel {
    private final MutableLiveData<User> userInfoResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<User> getUserInfoResult() {
        return userInfoResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchUserInfo(String email) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        RetrofitClient.getApiService().getUserInfo(body).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse.getCode() == 200) {
                        userInfoResult.setValue(apiResponse.getData());
                    } else {
                        error.setValue(apiResponse.getMsg());
                    }
                } else {
                    error.setValue("获取用户信息失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("MeViewModel", "获取用户信息失败: ", t);
            }
        });
    }
}
