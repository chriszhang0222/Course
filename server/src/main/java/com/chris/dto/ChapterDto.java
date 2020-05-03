package com.chris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 10:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {
    private String id;

    private String courseId;

    private String name;
}
