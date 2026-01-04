package com.example.zkaixian.pojo;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VideoDetail {
    @SerializedName("video_id")
    private String videoId;

    @SerializedName("video_name")
    private String videoName;
}