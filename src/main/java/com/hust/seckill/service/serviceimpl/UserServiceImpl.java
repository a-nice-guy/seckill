package com.hust.seckill.service.serviceimpl;


import com.alibaba.druid.util.StringUtils;
import com.hust.seckill.dao.UserDOMapper;
import com.hust.seckill.dao.UserPasswordDOMapper;
import com.hust.seckill.dataobject.UserDO;
import com.hust.seckill.dataobject.UserPasswordDO;
import com.hust.seckill.error.BusinessException;
import com.hust.seckill.error.EmBusinessError;
import com.hust.seckill.service.UserService;
import com.hust.seckill.service.model.UserModel;
import com.hust.seckill.validator.ValidationResult;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hzllb on 2018/11/11.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userDOmapper获取到对应的用户dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO,userPasswordDO);
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
        UserDO userDO = convertFromModel(userModel);
        //将UserDO保存至数据库中
        try{
            //telephone字段为唯一性索引，使用try catch捕获重复插入异常
            userDOMapper.insertSelective(userDO);
        }catch(DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        userModel.setId(userDO.getId());

        //将UserPasswordDO保存至数据库中
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        //获取用户密码信息并和用户信息一同合并为UserModel
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncryptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    //将UserModel转化为UserPasswordDO
    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    //将UserModel转化为UserDO
    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);

        return userDO;
    }

    //将UserPasswordDO和UserDO一同转化为UserModel
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);

        if(userPasswordDO != null){
            userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
        }

        return userModel;
    }
}
