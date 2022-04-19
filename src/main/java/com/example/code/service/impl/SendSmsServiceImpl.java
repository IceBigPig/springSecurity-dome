package com.example.code.service.impl;

import com.example.auth.utils.SendSmsUtils;
import com.example.code.service.SendSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: icebigpig
 * @Data: 2022/4/16 21:19
 * @Version 1.0
 **/

@Slf4j
@Service
public class SendSmsServiceImpl implements SendSmsService {
    private final static String VERIFY_CODE = "VERIFY_CODE";

    @Autowired
    private IMemberVerifyCodeRepository iMemberVerifyCodeRepository;

    @Override
    public String produceVerifyCode(String key) {

        if (iMemberVerifyCodeRepository.hasKey(key)) {
            this.deleteVerifyCode(key);
            //若redis中存在该验证码，删除该验证码
        }
        try {
            //得到生成的验证码，并且发送给用户手机端
            String verifyCode = SendSmsUtils.SendSms(key);
            //写入内存，方便验证
            iMemberVerifyCodeRepository.save(key, verifyCode);
            return verifyCode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public String getVerifyCode(String key){
        return iMemberVerifyCodeRepository.get(key);
    }

    @Override
    public void deleteVerifyCode(String key) {
        iMemberVerifyCodeRepository.delete(key);
    }
}
