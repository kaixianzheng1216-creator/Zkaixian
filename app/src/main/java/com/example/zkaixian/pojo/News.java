package com.example.zkaixian.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
@Data
public class News implements MultiItemEntity {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("newsName")
    private String newsName;

    @JsonProperty("newsTypeName")
    private String newsTypeName;

    @JsonProperty("img1")
    private String img1;

    @JsonProperty("img2")
    private String img2;

    @JsonProperty("img3")
    private String img3;

    @JsonProperty("newsUrl")
    private String newsUrl;

    @Override
    public int getItemType() {
        return getType();
    }
}