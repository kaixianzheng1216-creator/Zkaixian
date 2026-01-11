package com.example.zkaixian.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRetrofitClient {
    public static final String BASE_URL = "http://10.0.2.2:5000/api/";
    private static Retrofit retrofit = null;

    public static UserApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(UserApiService.class);
    }
}