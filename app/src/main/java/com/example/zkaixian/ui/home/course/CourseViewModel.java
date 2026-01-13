package com.example.zkaixian.ui.home.course;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.pojo.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseViewModel extends ViewModel {
    private final MutableLiveData<List<Course>> courseList = new MutableLiveData<>();

    public LiveData<List<Course>> getCourseList() {
        return courseList;
    }

    public void fetchCourses(int type) {
        Call<List<Course>> call;
        switch (type) {
            case 2:
                call = RetrofitClient.getApiService().getAlgorithmList();
                break;
            case 3:
                call = RetrofitClient.getApiService().getTechColumnList();
                break;
            case 4:
                call = RetrofitClient.getApiService().getOpenSourceList();
                break;
            case 1:
            default:
                call = RetrofitClient.getApiService().getCourseList();
                break;
        }

        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course>> call, @NonNull Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CourseViewModel", "课程请求成功" + response.body());
                    courseList.setValue(response.body());
                } else {
                    Log.e("CourseViewModel", "课程请求失败: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Course>> call, @NonNull Throwable t) {
                Log.e("CourseViewModel", "课程网络错误: " + t.getMessage());
            }
        });
    }
}
