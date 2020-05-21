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

package org.albireo.dto;

import lombok.Data;
import org.albireo.pojo.DashboardUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


/**
 * @author : venter.zhu
 * @version : 1.0
 * @email : zhutong@ijovo.com
 * @date :  2020/4/23 0023 21:08
 * JWT用户实体类
 **/

@Data
public class JwtUser implements UserDetails {

    private String username;
    private String password;
    private String userId;
    private String loginName;
    private String userStatus;
    /**
     * 角色集合
     */
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser() {
    }

    /**
     * 写一个能直接使用user创建jwtUser的构造器
     */
    public JwtUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public JwtUser(DashboardUser dashboardUser, Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        this.loginName = dashboardUser.getLoginName();
        this.username = dashboardUser.getUserName();
        this.password = dashboardUser.getUserPassword();
        this.userId = dashboardUser.getUserId();
        this.userStatus = dashboardUser.getUserStatus();
    }

    public JwtUser(String userId, String username, String loginName, Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        this.loginName = loginName;
        this.username = username;
        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "JwtUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
