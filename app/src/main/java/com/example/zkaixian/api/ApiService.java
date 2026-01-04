package com.example.zkaixian.api;

import com.example.zkaixian.pojo.Course;
import com.example.zkaixian.pojo.News;
import com.example.zkaixian.pojo.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("home_ad_list_data.json")
    Call<List<News>> getAdList();

    @GET("home_news_list_data.json")
    Call<List<News>> getNewsList();

    @GET("python_list_data.json")
    Call<List<Course>> getPythonList();

    @GET("video_list_data.json")
    Call<List<Video>> getVideoList();
}