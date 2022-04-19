package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.mapper.TbRoleMapper;
import com.example.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    TbRoleMapper tbRoleMapper;

    @Override
    public Set<String> getUserPermissions(int userId) {

        List<String> permissions = permissionMapper.getUserPermissions(userId);
        List<String> userRole = tbRoleMapper.getUserRole(userId);

        //合并两个 list
        userRole.stream().sequential().collect(Collectors.toCollection(() -> permissions));

        return permissions.stream().collect(Collectors.toCollection(HashSet::new));
    }
}
