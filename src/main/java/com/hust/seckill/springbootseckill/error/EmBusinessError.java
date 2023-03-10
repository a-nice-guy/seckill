package com.hust.seckill.springbootseckill.error;

public enum EmBusinessError implements CommonError {
    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),

    //20000开头为用户信息相关错误
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"登陆失败，用户手机号不存在或密码不正确"),
    USER_NOT_LOGIN(20003,"用户未登录"),
    //30000开头为交易信息错误
    STOCK_NOT_ENOUGH(30001,"库存不足"),
    TRANSACTION_CODE_ERROR(30002,"订单号错误"),
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
