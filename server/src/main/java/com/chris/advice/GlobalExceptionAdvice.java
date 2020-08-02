package com.chris.advice;

import com.chris.Exception.BusinessException;
import com.chris.util.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * @Author: Chris Zhang
 * @Date: 5/4/20 11:17
 */
@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResponse businessExceptionHandler(BusinessException e) {
        CommonResponse responseDto = new CommonResponse();
        responseDto.setSuccess(false);
        log.error("业务异常：{}", e.getCode().getDesc());
        responseDto.setMessage(e.getCode().getDesc());
        return responseDto;
    }
}
