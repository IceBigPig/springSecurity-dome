package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.TbRole;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Repository
public interface TbRoleMapper  extends BaseMapper<TbRole> {

    @Select("select role_name from tb_role where  id in  (SELECT role_id FROM tb_user_role WHERE user_id =#{userId})")
    List<String> getUserRole(int userId);
}
