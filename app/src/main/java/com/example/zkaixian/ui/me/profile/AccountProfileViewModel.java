package com.example.zkaixian.ui.me.profile;

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

public class AccountProfileViewModel extends ViewModel {
    private final MutableLiveData<User> updateResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<User> getUpdateResult() {
        return updateResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void updateProfile(String email, String username, String bio) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("username", username);
        body.put("bio", bio);

        RetrofitClient.getApiService().updateProfile(body).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse.getCode() == 200) {
                        updateResult.setValue(apiResponse.getData());
                    } else {
                        error.setValue(apiResponse.getMsg());
                    }
                } else {
                    error.setValue("个人资料更新失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("AccountProfileViewModel", "个人资料更新失败: ", t);
            }
        });
    }
}
