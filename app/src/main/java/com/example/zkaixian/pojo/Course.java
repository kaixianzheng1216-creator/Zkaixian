package com.example.zkaixian.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Course {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("address")
    private String address;

    @JsonProperty("content")
    private String content;

    @JsonProperty("open_class")
    private String openClass;
}