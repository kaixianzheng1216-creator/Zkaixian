package com.example.zkaixian.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@Data
public class Video {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("img")
    private String img;

    @JsonProperty("intro")
    private String intro;

    @JsonProperty("videoDetailList")
    private List<VideoDetail> videoDetailList;
}