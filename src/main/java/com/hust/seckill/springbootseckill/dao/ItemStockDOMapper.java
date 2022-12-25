package com.hust.seckill.springbootseckill.dao;

import com.hust.seckill.springbootseckill.dataobject.ItemStockDO;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStockDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);

    //通过Item_id从库存表中获得item库存
    ItemStockDO selectByItemId(Integer itemId);
}