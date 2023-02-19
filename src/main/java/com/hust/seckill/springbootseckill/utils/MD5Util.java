package com.hust.seckill.springbootseckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

public class MD5Util {

    private static final String salt = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";

    //获取随机salt
    public static String getRandomSalt() {
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<8; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(salt.charAt(number));
        }
        return sb.toString();
    }

    //MD5加密方法
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    //在前端使用固定盐进行加密
    public static String inputPassToFormPass(String inputPass) {
        //生成固定salt
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(4) + salt.charAt(6);
        return md5(str);
    }

    //在服务端使用随机盐进行加密
    public static String formPassToDBPass(String formPass, String randomSalt) {
        String str = randomSalt + formPass;
        return md5(str);
    }

    //
    public static String inputPassToDbPass(String inputPass, String randomSalt) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass,randomSalt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));//
        String randomSalt = getRandomSalt();
        System.out.println(randomSalt);
        System.out.println(formPassToDBPass(inputPassToFormPass("123456"),randomSalt));
//		System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));
    }
}
