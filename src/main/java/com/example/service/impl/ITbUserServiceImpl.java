package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.TbUser;
import com.example.mapper.TbUserMapper;
import com.example.service.ITbUserService;
import com.example.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Service
public class ITbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {


    @Autowired
    TbUserMapper tbUserMapper;

    @Autowired
    PermissionService permissionService;

    @Override
    public UserDetails getByEmail(String email) {
        TbUser user = tbUserMapper.selectOne(new QueryWrapper<TbUser>().eq("email", email));
        user.setListAuthorities(getAuthorities(user.getId()));
        return user;
    }

    @Override
    public UserDetails getByMobile(String mobile) {
        TbUser user = tbUserMapper.selectOne(new QueryWrapper<TbUser>().eq("mobile", mobile));
        user.setListAuthorities(getAuthorities(user.getId()));
        return user;
    }

    /**
     * 获取权限
     * @param userId
     * @return
     */
    private List<String> getAuthorities(int userId){
        Set<String> userPermissions = permissionService.getUserPermissions(userId);
        List<String> list = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userPermissions.toArray(new String[0]));
        for (GrantedAuthority grantedAuthority:authorities){
            list.add( grantedAuthority.getAuthority());
        }
        return list;
    }
}
