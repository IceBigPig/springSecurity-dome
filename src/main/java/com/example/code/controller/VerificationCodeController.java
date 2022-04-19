package com.example.code.controller;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.example.service.UserAccountCheck;
import com.example.code.entity.ImgTypeConstant;
import com.example.code.entity.VerifyVO;
import com.example.code.service.IVerifyCodeService;
import com.example.code.service.SendEmailService;
import com.example.code.service.SendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@RestController
@RequestMapping("/verifyImage")
public class VerificationCodeController {

    @Autowired
    private IVerifyCodeService iVerifyCodeService;

    @Autowired
    private SendSmsService sendSmsService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private UserAccountCheck userAccountCheck;

    /**
     * 获取验证码图片,
     * 验证码存储在redis中,key为uuid
     * @throws IOException
     */
    @GetMapping("/")
    public VerifyVO getVerifyImage() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            String verifyUuid = IdUtil.simpleUUID();
            Image image = iVerifyCodeService.getVerifyCodeImage(verifyUuid);

            ImageIO.write((RenderedImage)image, ImgTypeConstant.PNG.getSuffix(), byteArrayOutputStream);
            String verifyBase64 = Base64.encode(byteArrayOutputStream.toByteArray());

            verifyBase64 = "data:image/png;base64," + verifyBase64;

            VerifyVO verifyVO = new VerifyVO()
                    .setImg(verifyBase64)
                    .setUuid(verifyUuid);
            return verifyVO;
        }
    }

    /**
     * 发送邮件验证码
     * 写一个邮件服务 将生成的验证码存进redis数据库，后发送到填写的邮箱中。
     * 在登录的过滤器进行验证即可
     */
    @GetMapping("/email/{email}")
    public String emailCode(@PathVariable String email){

        if (userAccountCheck.CheckEmail(email)){
            //发送邮箱前检测该邮箱是否已经注册
            try {
                sendEmailService.SendEmail(email);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
            return "发送成功！";
        }
        else {
            return "该邮箱尚未注册！";
        }
    }

    /**
     * 发送手机短信验证码
     * 集成一个短信验证码 将生成的验证码存进redis数据库，后发送到填写的手机中。
     * 在登录的过滤器进行验证即可
     */
    @GetMapping("/mobile/{mobile}")
    public String mobileCode(@PathVariable String mobile){

        if(userAccountCheck.CheckMobile(mobile)) {
            //发送手机短信验证码前检测该手机号是否已经注册
            try {
                sendSmsService.produceVerifyCode(mobile);
                //调用短信发送服务执行发送短信验证码，并且写入到Redis赋值20min内有效
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "发送成功！";
        } else {
            return "该手机号尚未注册！";
        }
    }
}
