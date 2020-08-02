package com.chris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Chris Zhang
 * @Date: 5/4/20 19:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {

    private String id;
    private String title;
    private String courseId;
    private String chapterId;
    private String video;
    private Integer time;
    private String charge;
    private Integer sort;
    private String vod;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

}
