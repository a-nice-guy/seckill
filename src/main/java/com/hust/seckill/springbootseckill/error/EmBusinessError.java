package com.hust.seckill.springbootseckill.error;

public enum EmBusinessError implements CommonError{
    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(00001,"参数不合法"),
    UNKNOW_ERROR(10002,"未知错误"),

    //20000开头为用户信息相关错误
    USER_NOT_EXIST(20001,"用户不存在");
    ;

    private Integer errorCode;
    private String errorMsg;

    EmBusinessError(Integer errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
