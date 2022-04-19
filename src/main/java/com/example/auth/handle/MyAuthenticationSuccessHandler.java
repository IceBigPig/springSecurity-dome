package com.example.auth.handle;

import com.example.auth.utils.JwtTokenUtils;
import com.example.auth.utils.ResponseUtils;
import com.example.entity.TbUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 处理邮件验证登录成功处理
 * @Date: 2022-04-08 21:13
 * version 1.0
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        TbUser user = (TbUser) authentication.getPrincipal();

        List<String> roles=new ArrayList<>();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            roles.add( authority.getAuthority());
        }

        // 根据用户名，角色创建token并返回json信息
        String token = JwtTokenUtils.createToken(user.getUsername(), roles, false);

        user.setPassword(null);

        //这是为了让token 可视化而已
        user.setToken(JwtTokenUtils.TOKEN_PREFIX + token);

        response.setHeader("Bearer Token",token);

        ResponseUtils.result(response,HttpServletResponse.SC_OK,user);
    }
}
