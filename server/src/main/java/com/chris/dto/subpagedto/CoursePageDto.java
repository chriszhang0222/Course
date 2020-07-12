package com.chris.dto.subpagedto;

import com.chris.dto.PageDto;
import lombok.Data;

@Data
public class CoursePageDto extends PageDto {
    private String status;
    private String categoryId;
}
