package com.hust.seckill.springbootseckill.dao;

import com.hust.seckill.springbootseckill.DO.StockLogDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockLogDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_log
     *
     * @mbg.generated Fri Feb 10 14:49:06 CST 2023
     */
    int deleteByPrimaryKey(String stockLogId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_log
     *
     * @mbg.generated Fri Feb 10 14:49:06 CST 2023
     */
    int insert(StockLogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_log
     *
     * @mbg.generated Fri Feb 10 14:49:06 CST 2023
     */
    int insertSelective(StockLogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_log
     *
     * @mbg.generated Fri Feb 10 14:49:06 CST 2023
     */
    StockLogDO selectByPrimaryKey(String stockLogId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_log
     *
     * @mbg.generated Fri Feb 10 14:49:06 CST 2023
     */
    int updateByPrimaryKeySelective(StockLogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stock_log
     *
     * @mbg.generated Fri Feb 10 14:49:06 CST 2023
     */
    int updateByPrimaryKey(StockLogDO record);
}