package com.hust.seckill.springbootseckill.controller;

import com.hust.seckill.springbootseckill.controller.view.UserVO;
import com.hust.seckill.springbootseckill.error.BusinessException;
import com.hust.seckill.springbootseckill.error.EmBusinessError;
import com.hust.seckill.springbootseckill.response.CommonReturnType;
import com.hust.seckill.springbootseckill.service.UserService;
import com.hust.seckill.springbootseckill.service.model.UserModel;
import com.hust.seckill.springbootseckill.utils.MD5Util;
import com.hust.seckill.springbootseckill.utils.RedisConstants;
import com.hust.seckill.springbootseckill.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.hust.seckill.springbootseckill.utils.RedisConstants.USER_LOGIN_TTL;


@Controller("user")
@RequestMapping("/user")
public class UserController  extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 用户注册接口
     * @param telephone
     * @param otpCode
     * @param name
     * @param gender
     * @param age
     * @param password
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telephone")String telephone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpcode相符合
//        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        String inRedisOtpCode = (String) redisTemplate.opsForValue().get("user_register_validationCode_" + telephone);
        if(!StringUtils.equals(otpCode,inRedisOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        String randomSalt = MD5Util.getRandomSalt();
        userModel.setSalt(randomSalt);
        userModel.setEncryptPassword(MD5Util.inputPassToDbPass(password,randomSalt));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    /**
     * MD5加密
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    /**
     * 用户获取otp短信接口
     * @param telephone
     * @return
     */
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telephone")String telephone){

        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt =  random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联，使用httpsession的方式绑定他的手机号与OTPCODE
//        httpServletRequest.getSession().setAttribute(telephone,otpCode);

        //将用户申请的验证码存储至redis中
        redisTemplate.opsForValue().set("user_register_validationCode_" + telephone,otpCode);
        redisTemplate.expire("user_register_validationCode_" + telephone,5,TimeUnit.MINUTES);
        //将OTP验证码通过短信通道发送给用户,省略
        System.out.println("telephone = " + telephone + " & otpCode = "+otpCode);
        return CommonReturnType.create(null);
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO  = UserUtil.convertUserVOFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    /**
     * 用户登陆接口
     * @param telephone
     * @param password
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telephone")String telephone,
                                  @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //入参校验
        if(StringUtils.isEmpty(telephone)||
                StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登陆服务,用来校验用户登陆是否合法
        UserModel userModel = userService.validateLogin(telephone,password);

        //将登陆凭证加入到用户登陆成功的session内
//        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
//        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        //使用token+redis保存用户登录信息

        //获取token,以uuid的形式
        String uuidToken = UUID.randomUUID().toString();
        uuidToken = uuidToken.replace("-","");

        //建立token与用户登录态之间的联系
        redisTemplate.opsForValue().set(uuidToken, userModel);
        //设置过期时间
        redisTemplate.expire(uuidToken, USER_LOGIN_TTL, TimeUnit.HOURS);
        //下发token
        return CommonReturnType.create(uuidToken);
    }
}
