package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: icebigpig
 * @Data: 2022/4/18 21:39
 * @Version 1.0
 **/

@RestController
public class RegisterAccount {

    @RequestMapping("/register")
    String Register(){

        // TODO 需要在spring security中放行该接口

        // TODO 如果用户名/邮箱/手机号重复（已注册），返会前端报错信息

        // TODO 若信息经过验证可用，进行手机号与邮箱号进行绑定验证

        // TODO 若验证码错误，返回前端对应错误信息

        // TODO 若 手机号/邮箱 /手机号与邮箱 通过验证，写入到数据库

        return "注册成功！";
    }
}
