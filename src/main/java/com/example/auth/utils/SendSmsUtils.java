package com.example.auth.utils;

/**
 * @Author: icebigpig
 * @Data: 2022/4/16 17:49
 * @Version 1.0
 **/
// This file is auto-generated, don't edit it. Thanks.
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.*;
import org.springframework.stereotype.Service;

@Service
public class SendSmsUtils {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static String SendSms(String mobile) throws Exception {

        String random = RandomNum.getRandNum();

        com.aliyun.dysmsapi20170525.Client client = SendSmsUtils.createClient("阿里云api_key", "阿里云security");

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(mobile)
                .setTemplateParam("{\"code\":\""+ random +"\"}");

        //将该随机验证代码保存到redis，维持时效性
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
        return random;
    }
}
