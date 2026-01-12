package com.example.zkaixian.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.zkaixian.pojo.AmapTip;
import com.example.zkaixian.utils.AmapTipDeserializer;

public class AmapRetrofitClient {
    public static final String BASE_URL = "https://restapi.amap.com/v3/";
    private static Retrofit retrofit = null;

    public static AmapService getApiService() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AmapTip.class, new AmapTipDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit.create(AmapService.class);
    }
}