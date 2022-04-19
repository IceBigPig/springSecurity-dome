package com.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Data
@Accessors(chain = true)
public class Permission {
    private int id;
    private String code;
    private String description;
    private String url;
}
