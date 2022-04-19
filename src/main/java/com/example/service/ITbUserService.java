package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.TbUser;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
public interface ITbUserService extends IService<TbUser> {


    UserDetails getByEmail(String email);

    UserDetails getByMobile(String mobile);
}
