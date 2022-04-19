package com.example.auth;

import com.example.auth.email_code.EmailCodeAuthenticationFilter;
import com.example.auth.email_code.EmailCodeAuthenticationProvider;
import com.example.auth.filter.JwtAuthenticationFilter;
import com.example.auth.filter.JwtAuthorizationFilter;
import com.example.auth.handle.*;
import com.example.auth.mobile_code.MobileCodeAuthenticationFilter;
import com.example.auth.mobile_code.MobileCodeAuthenticationProvider;
import com.example.auth.service.Impl.UserDetailServiceImpl;
import com.example.code.service.IVerifyCodeService;
import com.example.service.ITbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 放行的路径
     */
    private final String[] PATH_RELEASE = {
            "/login",
            "/all",
            "/verifyImage/**"
    };
    /***根据用户名找到用户*/
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private MyAuthenticationEntryPoint macLoginUrlAuthenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private IVerifyCodeService iVerifyCodeService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                /**antMatchers (这里的路径)   permitAll 这里是允许所有人 访问*/
                .antMatchers(PATH_RELEASE).permitAll()
                /** 映射任何请求 */
                .anyRequest()

                /** 指定任何经过身份验证的用户都允许使用URL。*/
                .authenticated()

                /** 指定支持基于表单的身份验证 */
                .and().formLogin().permitAll()

                /** 允许配置异常处理。可以自己传值进去 使用WebSecurityConfigurerAdapter时，将自动应用此WebSecurityConfigurerAdapter 。*/
                .and().exceptionHandling()

                /** 设置要使用的AuthenticationEntryPoint。   macLoginUrlAuthenticationEntryPoint   验证是否登录*/
                .authenticationEntryPoint(macLoginUrlAuthenticationEntryPoint)

                /** 指定要使用的AccessDeniedHandler   处理拒绝访问失败。*/
                .accessDeniedHandler(myAccessDeniedHandler)

                /** 提供注销支持。 使用WebSecurityConfigurerAdapter时，将自动应用此WebSecurityConfigurerAdapter 。
                 *  默认设置是访问URL “ / logout”将使HTTP会话无效，清理配置的所有rememberMe()身份验证，清除SecurityContextHolder ，
                 *  然后重定向到“ / login？success”，从而注销用户*/
                .and().logout().logoutSuccessHandler(myLogoutSuccessHandler)


                .and()
                .authenticationProvider(emailCodeAuthenticationProvider())
                .addFilterBefore(emailCodeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .authenticationProvider(mobileCodeAuthenticationProvider())
                .addFilterBefore(mobileCodeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                /** 处理身份验证表单提交。 授予权限 */
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), iVerifyCodeService))
                /** 处理HTTP请求的BASIC授权标头，然后将结果放入SecurityContextHolder 。 */
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                /**不需要session */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);

    }



    /**
     * 密码加密
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 因为使用了BCryptPasswordEncoder来进行密码的加密，所以身份验证的时候也的用他来判断哈、，
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(emailCodeAuthenticationProvider()).authenticationProvider(mobileCodeAuthenticationProvider());
    }


    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;



    @Autowired
    ITbUserService userService;

    /**
     * nested exception is java.lang.IllegalArgumentException: authenticationManager must be specified 问题解决
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Bean
    public EmailCodeAuthenticationFilter emailCodeAuthenticationFilter() {
        EmailCodeAuthenticationFilter emailCodeAuthenticationFilter = new EmailCodeAuthenticationFilter();
        emailCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        emailCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        return emailCodeAuthenticationFilter;
    }

    @Bean
    public EmailCodeAuthenticationProvider emailCodeAuthenticationProvider() {
        return new EmailCodeAuthenticationProvider(userService);
    }

    @Bean
    public MobileCodeAuthenticationFilter mobileCodeAuthenticationFilter() {
        MobileCodeAuthenticationFilter mobileCodeAuthenticationFilter = new MobileCodeAuthenticationFilter();
        mobileCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        mobileCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        return mobileCodeAuthenticationFilter;
    }

    @Bean
    public MobileCodeAuthenticationProvider mobileCodeAuthenticationProvider() {
        return new MobileCodeAuthenticationProvider(userService);
    }

}
