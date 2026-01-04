package com.example.zkaixian.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@Data
public class Video {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("img")
    private String img;

    @SerializedName("intro")
    private String intro;

    @SerializedName("videoDetailList")
    private List<VideoDetail> videoDetailList;
}