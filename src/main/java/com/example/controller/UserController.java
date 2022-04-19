package com.example.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/all")
    String all() {
        return "在WebSecurityConfig中配置了放行，任何人都可以进行访问";
    }


    @PreAuthorize("permitAll()")
    @RequestMapping("/logged")
    String test() {
        return "所有登录的人都可以访问";
    }

    @PreAuthorize("hasRole('ROLE_VISITOR')")
    @RequestMapping("/role/visitor1")
    String visitor() {
        return "role: ROLE_VISITOR";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/role/user1")
    String userList() {
        return "role: ROLE_USER";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/role/admin1")
    String admin() {
        return "role: ROLE_ADMIN";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') ")
    @RequestMapping("/role/admin2")
    String adminAuth() {
        return "hasAuthority：ROLE_ADMIN";
    }

    @PreAuthorize("hasAuthority('ROLE_USER') ")
    @RequestMapping("/role/user2")
    String userAuth() {
        return "hasAuthority：ROLE_USER";
    }

    @PreAuthorize("hasAuthority('sys:select') ")
    @RequestMapping("/auth/select")
    String auth1() {
        return "hasAuthority：sys:select";
    }

    @PreAuthorize("hasAuthority('sys:add') ")
    @RequestMapping("/auth/add")
    String auth2() {
        return "hasAuthority：sys:add";
    }
}
