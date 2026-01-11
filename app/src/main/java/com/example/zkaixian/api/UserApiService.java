package com.example.zkaixian.api;

import com.example.zkaixian.common.ApiResponse;
import com.example.zkaixian.pojo.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {
    @POST("login")
    Call<ApiResponse<User>> login(@Body Map<String, String> body);

    @POST("register")
    Call<ApiResponse<User>> register(@Body Map<String, String> body);

    @POST("reset_password")
    Call<ApiResponse<Void>> resetPassword(@Body Map<String, String> body);

    @POST("update_profile")
    Call<ApiResponse<User>> updateProfile(@Body Map<String, String> body);

    @POST("send_code")
    Call<ApiResponse<Void>> sendCode(@Body Map<String, String> body);

    @POST("user_info")
    Call<ApiResponse<User>> getUserInfo(@Body Map<String, String> body);
}
