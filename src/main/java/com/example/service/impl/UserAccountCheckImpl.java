package com.example.service.impl;

import com.example.service.UserAccountCheck;
import com.example.mapper.TbUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: icebigpig
 * @Data: 2022/4/18 21:11
 * @Version 1.0
 **/

@Slf4j
@Service
public class UserAccountCheckImpl implements UserAccountCheck {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public boolean CheckEmail(String email){
        return tbUserMapper.checkEmail(email) != 0;
    }

    @Override
    public boolean CheckMobile(String mobile){
        return tbUserMapper.checkMobile(mobile) != 0;
    }

    @Override
    public boolean CheckUsername(String username){
        return tbUserMapper.checkUsername(username) != 0;
    }
}
