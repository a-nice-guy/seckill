package com.hust.seckill.springbootseckill.controller;

import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.error.EmBusinessError;
import com.hust.seckill.springbootseckill.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlers {

    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if( ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errorCode",businessException.getErrorCode());
            responseData.put("errorMsg",businessException.getErrorMsg());
        }else{
            responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errorMsg",EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(responseData,"fail");

    }

    //定义exceptionHandler处理controller层的exception
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerBusinessException(Exception ex, HttpServletRequest request){
        BusinessException businessException = (BusinessException) ex;
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("errorCode",businessException.getErrorCode());
        responseData.put("errorMsg",businessException.getErrorMsg());
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");
        commonReturnType.setData(responseData);
        return commonReturnType;
    }
}
