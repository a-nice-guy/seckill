package com.hust.seckill.springbootseckill.utils;

import com.hust.seckill.springbootseckill.controller.view.UserVO;
import com.hust.seckill.springbootseckill.dataobject.UserDO;
import com.hust.seckill.springbootseckill.dataobject.UserPasswordDO;
import com.hust.seckill.springbootseckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;

public class UserUtil {
    /**
     * 将核心领域模型userModel转换为userVO模型
     * @param userModel
     * @return
     */
    public static UserVO convertUserVOFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    //将UserModel转化为UserPasswordDO
    public static UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    //将UserModel转化为UserDO
    public static UserDO convertUserDOFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);

        return userDO;
    }

    //将UserPasswordDO和UserDO一同转化为UserModel
    public static UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
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
