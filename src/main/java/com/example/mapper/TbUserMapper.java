package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.RegisterUserVO;
import com.example.entity.TbUser;
import lombok.experimental.PackagePrivate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

    /**
     * 查询判断数据库中是否已经存在该信息，用于登录和注册时提示信息
     */
    public Integer checkEmail(@Param("email") String email);

    public Integer checkMobile(@Param("mobile") String mobile);

    public Integer checkUsername(@Param("username") String username);

    /*
     * 注册用户信息写入数据库
     */

    public void addUser(@Param("userinfo") RegisterUserVO registerUserVO);

}
