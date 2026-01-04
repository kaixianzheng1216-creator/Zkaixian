package com.example.zkaixian.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

@NoArgsConstructor
@Data
public class News implements MultiItemEntity {
    @SerializedName("id")
    private Integer id;

    @SerializedName("type")
    private Integer type;

    @SerializedName("newsName")
    private String newsName;

    @SerializedName("newsTypeName")
    private String newsTypeName;

    @SerializedName("img1")
    private String img1;

    @SerializedName("img2")
    private String img2;

    @SerializedName("img3")
    private String img3;

    @SerializedName("newsUrl")
    private String newsUrl;

    @Override
    public int getItemType() {
        return getType();
    }
}