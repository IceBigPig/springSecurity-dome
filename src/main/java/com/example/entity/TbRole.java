package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Data
@Accessors(chain = true)
public class TbRole {
    private String id;
    private String roleName;
    private String description;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;
    private String status;
}
