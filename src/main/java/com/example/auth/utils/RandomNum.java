package com.example.auth.utils;

import java.util.Random;

/**
 * @Author: icebigpig
 * @Data: 2022/4/16 20:07
 * @Version 1.0
 * 登录发送验证码生成器
 **/
public class RandomNum {

    public static String getRandNum() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

}
