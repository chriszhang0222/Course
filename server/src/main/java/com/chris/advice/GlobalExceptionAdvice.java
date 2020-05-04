package com.chris.advice;

import com.chris.util.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * @Author: Chris Zhang
 * @Date: 5/4/20 11:17
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = ValidationException.class)
    public CommonResponse<String> handleValidatorException(
            HttpServletRequest req, ValidationException ex
    ){

        CommonResponse<String> response = new CommonResponse<>("-1","validator error");
        response.setSuccess(false);
        response.setContent(ex.getMessage());
        return response;
    }
}
