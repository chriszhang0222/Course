package com.chris.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Chris Zhang
 * @Date: 5/3/20 19:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    private boolean success = true;
    private String code;
    private String message;
    private T content;

    public CommonResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public CommonResponse(String code, String message, T content){
        this.code = code;
        this.message = message;
        this.content = content;
    }

}
