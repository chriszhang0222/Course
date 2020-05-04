package com.chris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 17:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {

    private int page;
    private int size;
    private long total;

    private List<T> list;

}
