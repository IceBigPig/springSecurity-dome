package com.example.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.TbUser;
import com.example.mapper.TbRoleMapper;
import com.example.service.ITbUserService;
import com.example.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Date: 2022-04-11 21:13
 * version 1.0
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    ITbUserService userService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    TbRoleMapper tbRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbUser user = userService.getOne(new QueryWrapper<TbUser>().eq("username", username));
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
