package com.hust.seckill.springbootseckill.dao;

import com.hust.seckill.springbootseckill.DO.PromoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(PromoDO record);

    int insertSelective(PromoDO record);

    PromoDO selectByPrimaryKey(Integer id);

    /**
     * 根据itemId查询活动信息
     * @param itemId
     * @return
     */
    PromoDO selectByItemId(@Param("itemId") Integer itemId);

    int updateByPrimaryKeySelective(PromoDO record);

    int updateByPrimaryKey(PromoDO record);
}