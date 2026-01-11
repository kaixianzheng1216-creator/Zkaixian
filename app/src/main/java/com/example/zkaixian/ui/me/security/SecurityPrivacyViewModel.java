package com.example.zkaixian.ui.me.security;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.UserRetrofitClient;
import com.example.zkaixian.common.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecurityPrivacyViewModel extends ViewModel {
    private final MutableLiveData<Boolean> sendCodeResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updatePasswordResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<Boolean> getSendCodeResult() {
        return sendCodeResult;
    }

    public LiveData<Boolean> getUpdatePasswordResult() {
        return updatePasswordResult;
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
                Log.e("SecurityPrivacyViewModel", "验证码发送失败: ", t);
            }
        });
    }

    public void updatePassword(String email, String code, String newPassword) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("code", code);
        body.put("new_password", newPassword);

        UserRetrofitClient.getApiService().resetPassword(body).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        updatePasswordResult.setValue(true);
                    } else {
                        error.setValue(response.body().getMsg());
                    }
                } else {
                    error.setValue("密码修改失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("SecurityPrivacyViewModel", "密码修改失败: ", t);
            }
        });
    }
}
