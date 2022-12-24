package com.hust.seckill.springbootseckill.error;

public interface CommonError {

    public Integer getErrorCode();

    public String getErrorMsg();

    public CommonError setErrorMsg(String errorMsg);
}
