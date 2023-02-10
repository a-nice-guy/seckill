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
     * redis库存回滚
     * @param itemId
     * @param amount
     * @return
     * @throws BusinessException
     */
    boolean increaseStock(Integer itemId,Integer amount)throws BusinessException;

    /**
     * 库存扣减
     * @param itemId
     * @param amount
     * @return
     * @throws BusinessException
     */
    boolean decreaseStock(Integer itemId,Integer amount)throws BusinessException;

    /**
     * 异步扣减库存
     * @param itemId
     * @param amount
     * @return
     */
    boolean asyncDecreaseStock(Integer itemId,Integer amount);


    /**
     * 增加商品销量
     * @param itemId
     * @param amount
     * @throws BusinessException
     */
    void increaseSales(Integer itemId,Integer amount)throws BusinessException;

    /**
     * 从redis缓存中获item和promo model缓存
     * @param id
     * @return
     */
    ItemModel getItemByIdInCache(Integer id);

    /**
     * 初始化库存流水
     * @param itemId
     * @param amount
     */
    String initStockLog(Integer itemId, Integer amount);
}
