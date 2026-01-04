package com.example.zkaixian.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Course {
    @SerializedName("id")
    private Integer id;

    @SerializedName("address")
    private String address;

    @SerializedName("content")
    private String content;

    @SerializedName("open_class")
    private String openClass;
}