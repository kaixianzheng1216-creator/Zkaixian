package com.example.zkaixian.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

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