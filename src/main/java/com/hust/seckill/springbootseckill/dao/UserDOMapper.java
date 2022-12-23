package com.hust.seckill.springbootseckill.dao;


import com.hust.seckill.springbootseckill.dataobject.UserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    //与insert方法的区别在于当输入的某个属性值为null时，可以使用数据库预先设置的默认值（不使用null作为默认值）
    int insertSelective(UserDO record);
    UserDO selectByPrimaryKey(Integer id);

    UserDO selectByTelephone(String telephone);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}