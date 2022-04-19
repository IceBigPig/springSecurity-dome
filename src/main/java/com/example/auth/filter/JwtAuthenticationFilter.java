package com.example.auth.filter;

import com.example.auth.utils.JwtTokenUtils;
import com.example.auth.utils.ResponseUtils;
import com.example.code.service.IVerifyCodeService;
import com.example.entity.TbUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 处理身份验证表单提交。
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private IVerifyCodeService iVerifyCodeService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,IVerifyCodeService iVerifyCodeService) {
        this.authenticationManager = authenticationManager;

        this.iVerifyCodeService=iVerifyCodeService;
    }

    /**
     * 执行实际的身份验证。
     * 该实现应执行以下操作之一：
     * 返回已验证用户的已填充验证令牌，指示验证成功
     * 返回null，表示身份验证过程仍在进行中。 在返回之前，实现应执行完成该过程所需的任何其他工作。
     * 如果身份验证过程失败，则抛出AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        //输入流中获取到登录的信息
        try {
            TbUser loginUser = new ObjectMapper().readValue(request.getInputStream(), TbUser.class);
            /**
             * authenticate
             * 尝试对传递的Authentication对象进行身份Authentication ，
             * 如果成功，则返回完全填充的Authentication对象（包括授予的权限）
             * */

            //验证验证码 在接收的类里面也要添加相应的类型来接收
            iVerifyCodeService.verify(loginUser.getUuid(), loginUser.getVerifyCode());
            iVerifyCodeService.deleteVerifyCode(loginUser.getUuid());

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 成功验证后调用的方法
     * 如果验证成功，就生成token并返回
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        TbUser user = (TbUser) authResult.getPrincipal();

        List<String> roles=new ArrayList<>();

        // 因为在JwtUser中存了权限信息，可以直接获取，由于只有一个角色就这么干了
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

    /**
     * 验证失败时候调用的方法
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtils.result(response,HttpServletResponse.SC_FORBIDDEN,"登录失败，账号或密码错误");
    }
}
