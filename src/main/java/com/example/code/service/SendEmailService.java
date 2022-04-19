package com.example.code.service;

/**
 * @Author: icebigpig
 * @Data: 2022/4/17 13:56
 * @Version 1.0
 **/

public interface SendEmailService {

    /**
     * 发送邮件
     * @param receive    邮件收件人
     */
    public void SendEmail(String receive);

    String getVerifyCode(String key);

    void deleteVerifyCode(String key);
}
