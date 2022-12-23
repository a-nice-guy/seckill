package com.hust.seckill.error;

//装饰器模式，在EmBusinessError的基础上进行了功能上的增强，变成了一个exception
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    //接受EmBusinessError构造业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    //接受自定义的errorMsg构造业务异常
    public BusinessException(CommonError commonError, String errorMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrorMsg(errorMsg);
    }

    @Override
    public Integer getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    //疑问一？
    public CommonError setErrorMsg(String errorMsg) {
        this.commonError.setErrorMsg(errorMsg);
        return this;
    }
}
