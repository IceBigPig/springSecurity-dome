package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("SELECT code FROM tb_permission WHERE id IN( SELECT permission_id FROM tb_role_permission WHERE role_id IN(SELECT role_id FROM tb_user_role WHERE user_id = #{userId}))")
    List<String> getUserPermissions(int userId);
}
