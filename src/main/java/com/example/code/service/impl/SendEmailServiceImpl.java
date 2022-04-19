package com.example.code.service.impl;

import com.example.auth.utils.RandomNum;
import com.example.code.service.SendEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Author: icebigpig
 * @Data: 2022/4/17 14:00
 * @Version 1.0
 **/

@Slf4j
@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    private JavaMailSender mailSender;//注入QQ发送邮件的bean

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private IMemberVerifyCodeRepository iMemberVerifyCodeRepository;

    @Override
    public void SendEmail(String receive) {

        try{
            String verifyCode = RandomNum.getRandNum();

            //保存验证码到redis
            iMemberVerifyCodeRepository.save(receive,verifyCode);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(receive); // 接收地址
            message.setSubject("登录验证码"); // 标题
            message.setText("验证码是：" + verifyCode); // 内容
            mailSender.send(message);

        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getVerifyCode(String key){
        return iMemberVerifyCodeRepository.get(key);
    }

    @Override
    public void deleteVerifyCode(String key){
        iMemberVerifyCodeRepository.delete(key);
    }

}


