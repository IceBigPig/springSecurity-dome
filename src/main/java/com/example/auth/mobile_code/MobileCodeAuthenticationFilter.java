package com.example.auth.mobile_code;

import com.example.auth.utils.ResponseUtils;
import com.example.code.service.SendSmsService;
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
 * @Date: 2022-04-10 21:13
 * version 1.0
 */
public class MobileCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private SendSmsService sendSmsService;

    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

    public static final String SPRING_SECURITY_FORM_MOBILE_CODE_KEY = "mobileCode";

    private static final AntPathRequestMatcher MOBILE_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/mobile/login",
            "POST");

    /**
     * 前端传来的 参数名 - 用于request.getParameter 获取
     */
    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;

    private String codeParameter = SPRING_SECURITY_FORM_MOBILE_CODE_KEY;


    /**
     * 是否 仅仅post
     */
    private boolean postOnly = true;


    /**
     * 通过 传入的 参数 创建 匹配器
     * 即 Filter过滤的url
     */
    public MobileCodeAuthenticationFilter() {
        super(MOBILE_ANT_PATH_REQUEST_MATCHER);
    }


    public MobileCodeAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(MOBILE_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    /**
     * 给权限
     * filter 获得 用户名（邮箱） 和 密码（验证码） 装配到 token 上 ，
     * 然后把token 交给 provider 进行授权
     * 最后一步步返回，存储到安全上下文中
     *
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String mobile = getMobile(request);
        mobile = (mobile != null) ? mobile : "";
        mobile = mobile.trim();

        //如果 验证码不相等 故意让token出错 然后走springsecurity 错误的流程
        boolean flag = this.checkCode(request);

        // TODO 这边应当对查询的手机号码进行预先的数据库查询，如果不存在应当提示该账号不存在的信息

        if (!flag){
            // TODO 验证码错误，返回前端输出对应错误异常(待完善)
            ResponseUtils.result(response,HttpServletResponse.SC_FORBIDDEN,"验证码错误！");
            return null;
        }
        /**
         * 封装token
         */
        MobileCodeAuthenticationToken token = flag ? new MobileCodeAuthenticationToken(mobile, new ArrayList<>()) : new MobileCodeAuthenticationToken("error");

        this.setDetails(request, token);
            /*
            交给 manager 发证
             */
        return this.getAuthenticationManager().authenticate(token);

    }

    /**
     * 获取 头部信息 让合适的provider 来验证他
     *
     * @param request
     * @param authRequest
     */
    public void setDetails(HttpServletRequest request, MobileCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 获取 传来 的Email信息
     */
    public String getMobile(HttpServletRequest request) {
        String result = request.getParameter(this.mobileParameter);
        return result;
    }

    /**
     * 判断 传来的 验证码信息 以及 session 中的验证码信息
     */

    public boolean checkCode(HttpServletRequest request) {

        String code1 = request.getParameter(this.codeParameter);
        String mobile1 = request.getParameter(this.mobileParameter);
        /**
         * 获取前端传来的待验证的手机号码与验证码
         * 下面开始验证
         * 先从redis中获取是否存在该验证码，若存在，进行验证
         * 若不存在或者验证失败，返回前端对应的响应信息
         */

        //  另外再写一个链接 生成 验证码 那个验证码 在生成的时候  存进redis 中去
        //  这里的验证码 写在Redis中， 到时候取出来判断即可 验证之后 删除验证码

        if (Objects.equals(sendSmsService.getVerifyCode(mobile1), code1)) {
            System.out.println("验证通过！");
            sendSmsService.deleteVerifyCode(mobile1);
            //验证成功后删除该随机验证码
            return true;
        }
        else {
            return false;
        }
    }
}