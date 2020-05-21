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
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.albireo.dto.JwtUser;
import org.albireo.dto.Result;
import org.albireo.dto.ResultCodeEnum;
import org.albireo.util.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : venter.zhu
 * @version : 1.0
 * @date :  2020/4/23 0023 21:08
 * 认证拦截，判断有无认证，或者认证信息是否正确
 **/
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);// 从 http 请求头中取出 token
        try {
            // 执行认证
            if (tokenHeader == null) {
                chain.doFilter(request, response);
                return;
            }
            // 如果请求头中有token，则进行解析，并且设置认证信息
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
            super.doFilterInternal(request, response, chain);
        } catch (Exception e) {
            log.error("token校验失败：" + tokenHeader);
            e.printStackTrace();
            onUnsuccessfulAuthentication(request, response, null);
        }
    }

    /**
     * 解析token
     *
     * @param tokenHeader
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        String username;
        String userId;
        String loginName;
        String[] userRoles;
        try {
            username = JwtTokenUtils.getUsername(token);
            userId = JwtTokenUtils.getUserId(token);
            loginName = JwtTokenUtils.getLoginUserName(token);
            userRoles = JwtTokenUtils.getUserRole(token).split(",");
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            return null;
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String userRole : userRoles) {
            if (!StringUtils.isBlank(userRole)) {
                authorities.add(new SimpleGrantedAuthority(userRole));
            }
        }

        JwtUser jwtUser = new JwtUser(userId, username, loginName, authorities);
        if (username != null) {
            return new UsernamePasswordAuthenticationToken(jwtUser, null, authorities);
        }
        return null;
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(ResultCodeEnum.UNAUTHORIZED.getCode());
        // 统一处理token失效或者无效的问题
        String reason = JSONObject.toJSONString(Result.failure(ResultCodeEnum.UNAUTHORIZED));
        response.getWriter().write(reason);
    }
}
