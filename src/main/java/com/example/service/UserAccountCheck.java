package com.example.service;

/**
 * @Author: icebigpig
 * @Data: 2022/4/18 21:12
 * @Version 1.0
 **/
public interface UserAccountCheck {
    boolean CheckEmail(String email);

    boolean CheckMobile(String mobile);

    boolean CheckUsername(String username);
}
