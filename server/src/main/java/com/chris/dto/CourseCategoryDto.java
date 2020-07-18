package com.chris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCategoryDto {

    private String id;
    private String courseId;
    private String categoryId;
}
