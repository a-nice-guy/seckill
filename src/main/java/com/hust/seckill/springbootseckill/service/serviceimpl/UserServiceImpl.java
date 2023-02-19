package com.hust.seckill.springbootseckill.service.serviceimpl;


import com.alibaba.druid.util.StringUtils;
import com.hust.seckill.springbootseckill.dao.UserDOMapper;
import com.hust.seckill.springbootseckill.dao.UserPasswordDOMapper;
import com.hust.seckill.springbootseckill.dataobject.UserDO;
import com.hust.seckill.springbootseckill.dataobject.UserPasswordDO;
import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.error.EmBusinessError;
import com.hust.seckill.springbootseckill.service.UserService;
import com.hust.seckill.springbootseckill.service.model.UserModel;
import com.hust.seckill.springbootseckill.utils.MD5Util;
import com.hust.seckill.springbootseckill.utils.UserUtil;
import com.hust.seckill.springbootseckill.validator.ValidationResult;
import com.hust.seckill.springbootseckill.validator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userDOmapper获取到对应的用户dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return UserUtil.convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        //验证userModel是否为空
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //注册之前要对属性值进行合法性校验
        ValidationResult result =  validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //将UserModel转换dataobject方法，将用户信息存储至数据库中
        UserDO userDO = UserUtil.convertUserDOFromModel(userModel);
        //将UserDO保存至数据库中
        try{
            //telephone字段为唯一性索引，使用try catch捕获重复插入异常
            userDOMapper.insertSelective(userDO);
        }catch(DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        userModel.setId(userDO.getId());

        //将UserPasswordDO保存至数据库中
        UserPasswordDO userPasswordDO = UserUtil.convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        //获取用户密码信息并和用户信息一同合并为UserModel
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = UserUtil.convertFromDataObject(userDO,userPasswordDO);

        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        String passwordWithSalt = MD5Util.inputPassToDbPass(encrptPassword, userModel.getSalt());
        if(!StringUtils.equals(passwordWithSalt,userModel.getEncryptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public UserModel getUserByIdInCache(Integer id) {
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get("user_validate_"+id);
        if(userModel == null){
            userModel = this.getUserById(id);
            redisTemplate.opsForValue().set("user_validate_"+id,userModel);
            redisTemplate.expire("user_validate_"+id,10, TimeUnit.MINUTES);
        }
        return userModel;
    }
}
