package com.hust.seckill.springbootseckill.error;

import com.hust.seckill.springbootseckill.response.CommonReturnType;

public interface CommonError {

    public Integer getErrorCode();

    public String getErrorMsg();

    public CommonError setErrorMsg(String errorMsg);
}
