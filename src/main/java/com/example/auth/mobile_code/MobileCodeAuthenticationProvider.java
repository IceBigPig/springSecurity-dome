package com.example.auth.mobile_code;

import com.example.service.ITbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @Date: 2022-04-10 21:13
 * version 1.0
 */
@Slf4j
public class MobileCodeAuthenticationProvider implements AuthenticationProvider {


    ITbUserService userService;

    public MobileCodeAuthenticationProvider(ITbUserService userService) {
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
        log.info("MobileCodeAuthentication authentication request: %s", authentication);

        MobileCodeAuthenticationToken token = (MobileCodeAuthenticationToken) authentication;

        UserDetails user = userService.getByMobile((String) token.getPrincipal());

        System.out.println(token.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        System.out.println(user.getAuthorities());
        MobileCodeAuthenticationToken result =
                new MobileCodeAuthenticationToken(user, user.getAuthorities());
                /*
                Details 中包含了 ip地址、 sessionId 等等属性
                */
        result.setDetails(token.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return MobileCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
