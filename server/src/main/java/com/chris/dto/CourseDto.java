package com.chris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private String id;
    private String name;
    private String summary;
    private Integer time;
    private BigDecimal price;
    private String image;
    private String level;
    private String charge;
    private String status;
    private Integer enroll;
    private Integer sort;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatedAt;

    List<CategoryDto> categories;
    List<ChapterDto> chapters;
    List<SectionDto> sections;

    private String content;
    private TeacherDto teacher;
    private String teacherId;

}
