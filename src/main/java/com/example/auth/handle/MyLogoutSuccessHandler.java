package com.example.auth.handle;

import com.example.auth.utils.ResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出调用方法
 * @Date: 2022-04-10 21:13
 * version 1.0
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //TODO 业务操作 退出需要完成的事情
        ResponseUtils.result(response,HttpServletResponse.SC_OK,"退出成功");

    }
}
