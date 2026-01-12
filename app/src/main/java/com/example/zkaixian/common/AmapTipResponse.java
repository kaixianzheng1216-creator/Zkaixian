package com.example.zkaixian.common;

import com.example.zkaixian.pojo.AmapTip;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmapTipResponse {
    private String status;
    private String info;
    private String count;
    private List<AmapTip> tips;
}