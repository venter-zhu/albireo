/*
 * Copyright Albireo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.albireo.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.albireo.dto.JwtUser;
import org.albireo.dto.LoginUser;
import org.albireo.dto.Result;
import org.albireo.dto.ResultCodeEnum;
import org.albireo.util.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : venter.zhu
 * @version : 1.0
 * @date :  2020/4/23 0023 21:08
 * 登陆拦截器，实现登陆认证
 **/

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/auth/login");
    }

    /**
     * 接收并解析用户登陆信息，默认是/login
     * 为已验证的的用户返回一个已经生成的token的身份验证令牌，表示成功的身份验证
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        LoginUser loginUser;
        // 只允许 application/json
        if (!request.getContentType().contains("application/json")) {
            throw new AuthenticationServiceException("ContentType need application/json but there is " + request.getContentType());
        }
        // 只允许 POST
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 从输入流中获取到登录的信息
        try {
            loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
        } catch (IOException e) {
            log.error(e.toString());
            throw new AuthenticationServiceException("账号密码错误");
        }
        // 去 LoginAuthenticationProvider 中认证
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser, new ArrayList<>()));
    }

    /**
     * 成功验证后调用的方法
     * 如果验证成功，就生成token并返回
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

        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        log.warn("jwtUser:" + jwtUser.toString());
//        boolean isRemember = rememberMe.get()==null?false :rememberMe.get()==1;
//        boolean isMobile = loginType.get()==null?false:loginType.get() == 1;

        StringBuilder role = new StringBuilder();
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role.append(authority.getAuthority()).append(",");
        }
        logger.debug("登录成功");
        String token;
        token = JwtTokenUtils.createToken(jwtUser, role.toString(), true);
        // 返回创建成功的token，但是这里创建的token只是单纯的token，按照jwt的规定，最后请求的时候应该是 `Bearer token`
        response.setHeader(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + token);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        // body中返回token
        // 返回用户id
        Map<String, Object> data = new HashMap<>();
        data.put("token", JwtTokenUtils.TOKEN_PREFIX + token);
        data.put("userId", jwtUser.getLoginName());
        Result.success(data);
        String reason = JSONObject.toJSONString(Result.success(data));
        response.getWriter().write(reason);
    }

    /**
     * 认证失败的同一处理方法
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.debug("登录失败");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(ResultCodeEnum.UNAUTHORIZED.getCode());
        // 统一处理token失效或者无效的问题
        String reason = JSONObject.toJSONString(Result.failure(ResultCodeEnum.UNAUTHORIZED.getCode(), failed.getMessage()));
        response.getWriter().write(reason);
    }
}
