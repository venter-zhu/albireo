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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.albireo.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : venter.zhu
 * @version : 1.0
 * @email : zhutong@ijovo.com
 * @date :  2020/4/23 0023 21:08
 * 认证服务
 **/

@Component
@Slf4j
public class LoginAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "lightSwordUserDetailService")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // [1] token 中的用户名和密码都是用户输入的，不是数据库里的
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        LoginUser loginUser = (LoginUser) token.getCredentials();
        // 普通的外勤用户登陆
        // [2] 使用用户名从用户中心数据库读取用户信息
        //todo:自定义的消息无法被返回，而且这个方法里面的dao会被调用两次
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getName());

        // [3] 检查用户信息
        if (!userDetails.isEnabled()) {
            throw new DisabledException("用户已被禁用");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("账号已过期");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("账号已被锁定");
        } else if (!userDetails.isCredentialsNonExpired()) {
            throw new LockedException("凭证已过期");
        }

        // [4] 根据不同的情况比对密码
        // 数据库用户的密码，一般都是加密过的
        String encryptedPassword = userDetails.getPassword();
//                if(!encryptedPassword.startsWith("$2a$10$")){
//                    throw new BadCredentialsException("密码不允许设置为明文！");
//                }
        // 用户输入的密码
        String inputPassword = loginUser.getPassword();

        log.warn(passwordEncoder.encode(inputPassword));

        // 根据加密算法加密用户输入的密码，然后和数据库中保存的密码进行比较
        if (StringUtils.isNotBlank(inputPassword) && passwordEncoder.matches(inputPassword, encryptedPassword)) {

        } else{
            // 用户输入跟数据库存储的都是明文或者密文
            if (StringUtils.equals(encryptedPassword, inputPassword)) {

            } else {
                throw new UsernameNotFoundException("用户名或者密码错误！");
            }
        }


        // [5] 成功登陆，把用户信息提交给 Spring Security
        // 把 userDetails 作为 principal 的好处是可以放自定义的 UserDetails，这样可以存储更多有用的信息，而不只是 username，
        // 默认只有 username，这里的密码使用数据库中保存的密码，而不是用户输入的明文密码，否则就暴露了密码的明文
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    /**
     * 判断是否通过 OAuth 登陆的用户，例如 QQ，优酷等都提供了 OAuth 第三方登陆服务。
     *
     * @param userDetails
     * @return 如果是 OAuth 用户则返回 true，否则返回 false
     */
    private boolean isOAuthUser(UserDetails userDetails) {
        // 以 QQ_ 开头的用户名，说明是使用 QQ 的 OAuth 登陆的用户
        // 以 YK_ 开头的用户名，说明是使用优酷的 OAuth 登陆的用户
        // ...
        return userDetails.getUsername().startsWith("QQ_");
    }
}