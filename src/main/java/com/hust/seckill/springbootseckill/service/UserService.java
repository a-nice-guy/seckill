package com.hust.seckill.springbootseckill.service;


import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.service.model.UserModel;

public interface UserService {


    /**
     * 通过用户ID获取用户对象
     * @param id
     * @return
     */
    UserModel getUserById(Integer id);

    /**
     * 注册用户信息
     * @param userModel
     * @throws BusinessException
     */
    void register(UserModel userModel) throws BusinessException;

    /**
     * 验证用户登录合法性
     * @param telphone
     * @param encrptPassword
     * @return
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException;
}
