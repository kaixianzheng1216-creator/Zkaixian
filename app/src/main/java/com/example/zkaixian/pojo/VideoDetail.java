package com.example.zkaixian.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class VideoDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("video_id")
    private String videoId;

    @SerializedName("video_name")
    private String videoName;
}