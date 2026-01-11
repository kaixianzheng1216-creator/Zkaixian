package com.example.zkaixian.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {
    private Integer id;
    private String email;
    private String username;
    private String bio;
    private Integer course_count;
    private String study_time;
    private Integer certificate_count;
}