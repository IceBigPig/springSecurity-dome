package com.example.auth.mobile_code;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Date: 2022-04-10 21:13
 * version 1.0
 */
public class MobileCodeAuthenticationToken extends AbstractAuthenticationToken {


    /**
     * 这里的 principal 指的是 mobile 地址（未认证的时候）
     */
    private final Object principal;

    public MobileCodeAuthenticationToken(Object principal) {
        super((Collection) null);
        this.principal = principal;
        setAuthenticated(false);
    }

    public MobileCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }


}
