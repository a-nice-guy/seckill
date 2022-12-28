package com.hust.seckill.springbootseckill.controller;


import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.error.EmBusinessError;
import com.hust.seckill.springbootseckill.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";


//    //定义exceptionhandler解决未被controller层吸收的exception
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Object handlerException(HttpServletRequest request, Exception ex){
//        Map<String,Object> responseData = new HashMap<>();
//        if( ex instanceof BusinessException){
//            BusinessException businessException = (BusinessException)ex;
//            responseData.put("errorCode",businessException.getErrorCode());
//            responseData.put("errorMsg",businessException.getErrorMsg());
//        }else{
//            responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
//            responseData.put("errorMsg",EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
//        }
//        return CommonReturnType.create(responseData,"fail");
//
//    }
}
