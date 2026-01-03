package com.example.zkaixian.api;

import com.example.zkaixian.pojo.News;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("home_ad_list_data.json")
    Call<List<News>> getAdList();

    @GET("home_news_list_data.json")
    Call<List<News>> getNewsList();
}