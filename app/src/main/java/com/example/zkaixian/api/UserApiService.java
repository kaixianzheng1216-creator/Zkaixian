package com.example.zkaixian.api;

import com.example.zkaixian.common.ApiResponse;
import com.example.zkaixian.pojo.Address;
import com.example.zkaixian.pojo.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

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

    @GET("addresses")
    Call<ApiResponse<List<Address>>> getAddresses(@QueryMap Map<String, String> params);

    @POST("addresses")
    Call<ApiResponse<Address>> addAddress(@Body Map<String, String> body);

    @DELETE("addresses/{id}")
    Call<ApiResponse<Void>> deleteAddress(@Path("id") int id);
}
