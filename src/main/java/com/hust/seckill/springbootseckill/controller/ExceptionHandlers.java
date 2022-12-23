package com.hust.seckill.springbootseckill.controller;

import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.error.EmBusinessError;
import com.hust.seckill.springbootseckill.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlers {

    //定义处理父类exception的方法
    @ExceptionHandler(Exception.class)
    public Object handlerException(){
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("errorCode", EmBusinessError.UNKNOW_ERROR.getErrorCode());
        responseData.put("errorMsg",EmBusinessError.UNKNOW_ERROR.getErrorMsg());
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");
        commonReturnType.setData(responseData);
        return commonReturnType;
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
