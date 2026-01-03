package com.example.zkaixian.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VideoDetail {
    @JsonProperty("video_id")
    private String videoId;

    @JsonProperty("video_name")
    private String videoName;
}