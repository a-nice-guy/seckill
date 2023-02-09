package com.hust.seckill.springbootseckill.service;


import com.hust.seckill.springbootseckill.service.model.PromoModel;

public interface PromoService {
    //根据itemid获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

    //将活动商品加入缓存
    void publicPromo(Integer promoId);
}
