package com.hust.seckill.springbootseckill.service;


import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.service.model.UserModel;

/**
 * Created by hzllb on 2018/11/11.
 */
public interface UserService {
    //通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;

    /*
    telphone:用户注册手机
    password:用户加密后的密码
     */
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
