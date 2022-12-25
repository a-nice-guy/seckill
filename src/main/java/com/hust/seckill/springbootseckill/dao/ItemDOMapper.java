package com.hust.seckill.springbootseckill.dao;

import com.hust.seckill.springbootseckill.dataobject.ItemDO;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);
}