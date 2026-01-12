package com.example.zkaixian.ui.me.address.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.AmapRetrofitClient;
import com.example.zkaixian.pojo.AmapTip;
import com.example.zkaixian.common.AmapTipResponse;

import java.util.List;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressSearchViewModel extends ViewModel {
    private final MutableLiveData<List<AmapTip>> searchResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private static final String AMAP_KEY = "06fa150c0cf58e5c645a06d9317c3978";

    public LiveData<List<AmapTip>> getSearchResult() {
        return searchResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void searchAddress(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("key", AMAP_KEY);
        params.put("keywords", keyword);

        AmapRetrofitClient.getApiService().getInputTips(params).enqueue(new Callback<AmapTipResponse>() {
            @Override
            public void onResponse(Call<AmapTipResponse> call, Response<AmapTipResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AmapTipResponse apiResponse = response.body();

                    if ("1".equals(apiResponse.getStatus())) {
                        searchResult.setValue(apiResponse.getTips());
                    } else {
                        error.setValue("搜索失败: " + apiResponse.getInfo());
                    }
                } else {
                    error.setValue("搜索失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AmapTipResponse> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("AddressSearchViewModel", "搜索失败", t);
            }
        });
    }
}
