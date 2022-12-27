package com.hust.seckill.springbootseckill.service;

import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    /**
     * 创建商品
     * @param itemModel
     * @return
     * @throws BusinessException
     */
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    /**
     * 浏览商品列表
     * @return
     */
    List<ItemModel> listItem();

    /**
     * 浏览商品详情
     * @param id
     * @return
     */
    ItemModel getItemById(Integer id);

    /**
     * 库存扣减
     * @param itemId
     * @param amount
     * @return
     * @throws BusinessException
     */
    boolean decreaseStock(Integer itemId,Integer amount)throws BusinessException;

    /**
     * 增加商品销量
     * @param itemId
     * @param amount
     * @throws BusinessException
     */
    void increaseSales(Integer itemId,Integer amount)throws BusinessException;

}
