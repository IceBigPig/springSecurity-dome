package com.example.auth.email_code;


import com.example.service.ITbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * 邮件验证过滤器
 * @Author: icebigpig
 * @Date: 2022-04-08 21:13
 * version 1.0
 */
@Slf4j
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    ITbUserService userService;

    public EmailCodeAuthenticationProvider(ITbUserService userService) {
        this.userService = userService;
    }


    /**
     * 认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        log.info("EmailCodeAuthentication authentication request: %s", authentication);
        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken) authentication;
        // 从数据库查询 数据
        UserDetails user = userService.getByEmail((String) token.getPrincipal());

        System.out.println(token.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        System.out.println(user.getAuthorities());

        EmailCodeAuthenticationToken result =
                new EmailCodeAuthenticationToken(user, user.getAuthorities());
        /**
        Details 中包含了 ip地址、 sessionId 等等属性
        其实还可以存储一些我们想要存储的数据，之后我们再利用。、
        */
        result.setDetails(token.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}