package com.example.entity;

import lombok.*;

/**
 * @Author: icebigpig
 * @Data: 2022/4/18 20:57
 * @Version 1.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserVO {

    private String username;

    private String password;

    private String fullname;

    private String mobile;

    private String email;

}