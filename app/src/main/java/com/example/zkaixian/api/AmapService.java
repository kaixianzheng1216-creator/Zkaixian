package com.example.zkaixian.api;

import com.example.zkaixian.common.AmapTipResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface AmapService {
    @GET("assistant/inputtips")
    Call<AmapTipResponse> getInputTips(@QueryMap Map<String, String> params);
}