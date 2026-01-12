package com.example.zkaixian.ui.me.address.list;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.UserRetrofitClient;
import com.example.zkaixian.common.ApiResponse;
import com.example.zkaixian.pojo.Address;

import java.util.List;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressListViewModel extends ViewModel {
    private final MutableLiveData<List<Address>> addressList = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();

    public LiveData<List<Address>> getAddressList() {
        return addressList;
    }

    public LiveData<Boolean> getDeleteResult() {
        return deleteResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchAddresses(String email) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        
        UserRetrofitClient.getApiService().getAddresses(params).enqueue(new Callback<ApiResponse<List<Address>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Address>>> call, Response<ApiResponse<List<Address>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Address>> apiResponse = response.body();

                    if (apiResponse.getCode() == 200) {
                        addressList.setValue(apiResponse.getData());
                    } else {
                        error.setValue(apiResponse.getMsg());
                    }
                } else {
                    error.setValue("获取地址列表失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Address>>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("AddressListViewModel", "获取地址列表失败", t);
            }
        });
    }

    public void deleteAddress(int id) {
        UserRetrofitClient.getApiService().deleteAddress(id).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        deleteResult.setValue(true);
                    } else {
                        error.setValue(apiResponse.getMsg());
                    }
                } else {
                    error.setValue("删除失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                error.setValue("网络错误: " + t.getMessage());
                Log.e("AddressListViewModel", "删除失败", t);
            }
        });
    }
}
