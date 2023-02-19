package com.hust.seckill.springbootseckill.controller;

import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.error.EmBusinessError;
import com.hust.seckill.springbootseckill.rocketmq.MqProducer;
import com.hust.seckill.springbootseckill.response.CommonReturnType;
import com.hust.seckill.springbootseckill.service.ItemService;
import com.hust.seckill.springbootseckill.service.OrderService;
import com.hust.seckill.springbootseckill.service.PromoService;
import com.hust.seckill.springbootseckill.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;

@Controller("order")
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MqProducer mqProducer;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PromoService promoService;

    private ExecutorService executorService;

    @PostConstruct
    public void init(){
        executorService = Executors.newFixedThreadPool(20);
    }

    //封装下单请求
    @RequestMapping(value = "/generatetoken",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType generateToken(@RequestParam(name = "token") String token,
                                        @RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="promoId")Integer promoId) throws BusinessException {

        //使用token判断用户登录状态
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能获取令牌");
        }

        //获取用户的登陆信息
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能获取令牌");
        }

        //获取当前活动的秒杀令牌
        String promoToken = promoService.generateSeckillToken(promoId, userModel.getId(), itemId);

        if (promoToken == null) {
            throw new BusinessException((EmBusinessError.PARAMETER_VALIDATION_ERROR),"生成令牌失败");
        }


        return CommonReturnType.create(promoToken);
    }

    //封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "token") String token,
                                        @RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="amount")Integer amount,
                                        @RequestParam(name="promoId",required = false)Integer promoId,
                                        @RequestParam(name="promoToken",required = false)String promoToken) throws BusinessException {

//        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
//        if(isLogin == null || !isLogin.booleanValue()){
//            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
//        }

        //使用token判断用户登录状态
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
        }

        //获取用户的登陆信息
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
        }

        //验证秒杀token是否正确
        if (promoId != null) {
            String tokenInRedis = (String)redisTemplate.opsForValue().get("promo_token_" + promoId + "_userId_" + userModel.getId()+ "_item_" + itemId);
            boolean equals = StringUtils.equals(tokenInRedis, promoToken);
            if (tokenInRedis == null) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"秒杀令牌校验失败");
            }
            if (!equals) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"秒杀令牌校验失败");
            }
        }

        //同步调用线程池的submit方法
        //拥塞窗口为20的等待队列，用来队列化泄洪
        Future<Object> future = executorService.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                //初始化库存流水信息
                String stockLogId = itemService.initStockLog(itemId, amount);

//        OrderModel orderModel = orderService.createOrder(userModel.getId(),itemId,promoId,amount,stockLogId);
                boolean result = mqProducer.transactionAsyncReduceStock(userModel.getId(), itemId, promoId, amount, stockLogId);
                if (result == false) {
                    throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"下单失败");
                }
                return null;
            }
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        } catch (ExecutionException e) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }

        return CommonReturnType.create(null);
    }
}
