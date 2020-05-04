package com.chris.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 19:56
 */
@Data
@NoArgsConstructor
public class CommonResponse<T> {

    private boolean success = true;
    private String code;
    private String message;
    private T content;

    public CommonResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

}
