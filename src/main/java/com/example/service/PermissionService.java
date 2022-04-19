package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Permission;

import java.util.Set;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
public interface PermissionService extends IService<Permission> {
    Set<String> getUserPermissions(int userId);
}
