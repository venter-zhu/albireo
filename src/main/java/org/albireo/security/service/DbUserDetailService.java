/*
 * Copyright Cboard
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

package org.albireo.security.service;

import org.albireo.dto.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by yfyuan on 2016/12/14.
 */
public class DbUserDetailService extends JdbcDaoImpl {

    protected List<UserDetails> loadUsersByUsername(final String username) {
        return this.getJdbcTemplate().query(super.getUsersByUsernameQuery(), new String[]{username}, new RowMapper() {
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                String userId = rs.getString(1);
                String username = rs.getString(2);
                String loginname = rs.getString(3);
                String password = rs.getString(4);
                boolean enabled = rs.getBoolean(5);
                User user = new User(loginname, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
                user.setUserId(userId);
                user.setName(username);
                return user;
            }
        });
    }

    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        return userFromUserQuery;
    }

}
