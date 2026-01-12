package com.example.zkaixian.pojo;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmapTip implements Serializable {
    private String id;
    private String name;
    private String district;
    private String adcode;
    private String location;
    private String address;
    private String typecode;
}