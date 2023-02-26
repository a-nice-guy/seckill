package com.hust.seckill.springbootseckill.utils;

import com.hust.seckill.springbootseckill.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.hust.seckill.springbootseckill.utils.RedisConstants.USER_LOGIN_TTL;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    public RefreshTokenInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getParameter("token");

        if (StringUtils.isEmpty(token)) {
            return true;
        }
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if (userModel == null) {
            return true;
        }
        //将数据保存到Threadlocal中
        UserHolder.saveUser(userModel);

        //刷新token有效期
        redisTemplate.expire(token,USER_LOGIN_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserHolder.removeUser();
    }
}
