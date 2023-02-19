package com.hust.seckill.springbootseckill.dao;

import com.hust.seckill.springbootseckill.dataobject.ItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);

    //获取Item list
    List<ItemDO> listItem();

    //增加产品销量
    int increaseSales(@Param("amount") Integer amount, @Param("id") Integer id);
}