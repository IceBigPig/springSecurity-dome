package com.example.auth.email_code;

import com.example.auth.utils.ResponseUtils;
import com.example.code.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * 邮件验证过滤器
 * @Author: icebigpig
 * @Date: 2022-04-08 21:13
 * version 1.0
 */
public class EmailCodeAuthenticationFilter  extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private SendEmailService sendEmailService;

    /**
     * 前端传来的 参数名 - 用于request.getParameter 获取
     */
    private final String DEFAULT_EMAIL_NAME="email";

    private final String DEFAULT_EMAIL_CODE="e_code";

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
    /**
     * 是否 仅仅post
     */
    private boolean postOnly = true;

    /**
     * 通过 传入的 参数 创建 匹配器
     * 即 Filter过滤的url
     */
    public EmailCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/email/login","POST"));
    }


    /**
     * 给权限
     * filter 获得 用户名（邮箱） 和 密码（验证码） 装配到 token 上 ，
     * 然后把token 交给 provider 进行授权
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(postOnly && !request.getMethod().equals("POST") ){
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }else{

            String email = getEmail(request);
            if(email == null){
                email = "";
            }
            email = email.trim();


            //如果 验证码不相等 故意让token出错 然后走springsecurity 错误的流程
            boolean flag = this.checkCode(request);

            // TODO 这边应当对查询的手机号码进行预先的数据库查询，如果不存在应当提示该账号不存在的信息

            if (!flag){
                // TODO 验证码错误，返回前端输出对应错误异常(待完善)
                ResponseUtils.result(response,HttpServletResponse.SC_FORBIDDEN,"验证码错误！");
                return null;
            }

            //封装 token
            EmailCodeAuthenticationToken token = null;
            if(flag){
                token  = new EmailCodeAuthenticationToken(email,new ArrayList<>());
            }else{
                token = new EmailCodeAuthenticationToken("error");
            }
            //TODO 这里可以存一些我们可以利用的数据 如:存那个是否记住我，ip地址啊
            this.setDetails(request,token);
            //交给 AuthenticationManager 进行认证 这个流程中在代码中有说明
            return this.getAuthenticationManager().authenticate(token);
        }
    }



    public void setDetails(HttpServletRequest request , EmailCodeAuthenticationToken token ){
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 获取 传来 的Email信息
     */
    public String getEmail(HttpServletRequest request ){
        String result=  request.getParameter(DEFAULT_EMAIL_NAME);
        return result;
    }

    /**
     * 判断 传来的 验证码信息 以及 session 中的验证码信息
     */
    public boolean checkCode(HttpServletRequest request ){
        String code1 = request.getParameter(DEFAULT_EMAIL_CODE);
        String email1 = request.getParameter(DEFAULT_EMAIL_NAME);

        // TODO 另外再写一个链接 生成 验证码 那个验证码 在生成的时候  存进redis 中去
        // TODO  这里的验证码 写在Redis中， 到时候取出来判断即可 验证之后 删除验证码
        if (Objects.equals(sendEmailService.getVerifyCode(email1), code1)) {
            System.out.println("验证通过！");
            sendEmailService.deleteVerifyCode(email1);
            //验证成功后删除该随机验证码
            return true;
        }
        else {
            return false;
        }
    }
}