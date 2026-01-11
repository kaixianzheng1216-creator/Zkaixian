package com.example.zkaixian.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private int code;
    private String msg;
    private T data;
}