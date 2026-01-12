package com.example.zkaixian.pojo;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String address;
    private String detail;
    
    // @SerializedName("is_default")
    // private boolean isDefault;
    
    private String email;
}
