package com.example.code.service;

/**
 * @Author: icebigpig
 * @Data: 2022/4/16 21:16
 * @Version 1.0
 **/

public interface SendSmsService {
    /**
     * 得到验证码
     * @param key 当前验证码的key
     * @return 图片
     */
    String produceVerifyCode(String key);

    /**
     * 从内存中得到原始验证码
     * 并去检前端传过来的验验证码是否正确
     *
     * #@param phone 验证验的phone
     * #@param code  待校验的验证码
     * @return
     */
//    boolean verify(String phone, String code);

    String  getVerifyCode(String key);

    /**
     * 删除验证码
     * @param phone 验证验的key
     */
    void deleteVerifyCode(String phone);
}
