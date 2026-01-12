package com.example.zkaixian.ui.me.address.add;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.UserRetrofitClient;
import com.example.zkaixian.common.ApiResponse;
import com.example.zkaixian.pojo.Address;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAddViewModel extends ViewModel {
    private final MutableLiveData<Boolean> addResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<Boolean> getAddResult() {
        return addResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void addAddress(String email, Address address) {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", address.getName());
        map.put("phone", address.getPhone());
        map.put("address", address.getAddress());
        map.put("detail", address.getDetail());
        
        UserRetrofitClient.getApiService().addAddress(map).enqueue(new Callback<ApiResponse<Address>>() {
            @Override
            public void onResponse(Call<ApiResponse<Address>> call, Response<ApiResponse<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Address> apiResponse = response.body();

                    if (apiResponse.getCode() == 200) {
                        addResult.setValue(true);
                    } else {
                        error.setValue(apiResponse.getMsg());
                    }
                } else {
                    error.setValue("保存失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Address>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("AddressAddViewModel", "添加地址失败", t);
            }
        });
    }
}
