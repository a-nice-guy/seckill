package com.hust.seckill.springbootseckill.service;

import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.service.model.ItemModel;

import java.util.List;

/**
 * Created by hzllb on 2018/11/18.
 */
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

//    //商品销量增加
//    void increaseSales(Integer itemId,Integer amount)throws BusinessException;

}
