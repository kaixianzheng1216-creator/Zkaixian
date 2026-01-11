package com.example.zkaixian.ui.me.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.UserRetrofitClient;
import com.example.zkaixian.common.ApiResponse;
import com.example.zkaixian.pojo.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<User> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void login(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        UserRetrofitClient.getApiService().login(body).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        loginResult.setValue(apiResponse.getData());
                    } else {
                        error.setValue(apiResponse.getMsg());
                    }
                } else {
                    error.setValue("登录失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("LoginViewModel", "登录失败: ", t);
            }
        });
    }
}
