package com.example.code.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cuberxp
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Data
@Accessors(chain = true)
public class VerifyVO {
    private String img;
    private String uuid;
}
