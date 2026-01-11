package com.example.zkaixian.ui.me.register;

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

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<User> registerResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendCodeResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<User> getRegisterResult() {
        return registerResult;
    }

    public LiveData<Boolean> getSendCodeResult() {
        return sendCodeResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void sendCode(String email) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        UserRetrofitClient.getApiService().sendCode(body).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        sendCodeResult.setValue(true);
                    } else {
                        error.setValue(response.body().getMsg());
                    }
                } else {
                    error.setValue("验证码发送失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("RegisterViewModel", "验证码发送失败: ", t);
            }
        });
    }

    public void register(String email, String password, String username, String code) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("username", username);
        body.put("code", code);

        UserRetrofitClient.getApiService().register(body).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        registerResult.setValue(apiResponse.getData());
                    } else {
                        error.setValue(apiResponse.getMsg());
                    }
                } else {
                    error.setValue("注册失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("RegisterViewModel", "注册失败: ", t);
            }
        });
    }
}
