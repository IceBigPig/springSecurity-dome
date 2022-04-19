package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TbUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String password;
    private String fullname;
    private String mobile;

    private String email;

    @TableField(exist = false)
    private String uuid;

    @TableField(exist = false)
    private String verifyCode;


    // 1:启用 ， 0：禁用
    @TableField(exist = false)
    private Integer enabled = 1;

    // 1：锁住 ， 0：未锁
    @TableField(exist = false)
    private Integer locked = 0;


    @TableField(exist = false)
    private List<String> listAuthorities;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private String e_code;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String authority:listAuthorities){
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(authority);
            authorities.add(auth);
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled == 1;
    }
}
