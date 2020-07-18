package com.chris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseContentFileDto {

    private String id;
    private String courseId;
    private String url;
    private String name;
    private Integer size;
}
