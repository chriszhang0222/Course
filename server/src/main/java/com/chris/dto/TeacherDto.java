package com.chris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {

    private String id;
    private String name;
    private String nickname;
    private String image;
    private String position;
    private String intro;
}
