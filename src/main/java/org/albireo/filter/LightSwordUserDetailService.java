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
import org.apache.commons.lang3.StringUtils;
import org.albireo.dao.UserDao;
import org.albireo.dto.JwtUser;
import org.albireo.pojo.DashboardUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * @author : venter.zhu
 * @version : 1.0
 * @date :  2020/4/23 0023 21:08
 **/
@Service
@Slf4j
public class LightSwordUserDetailService implements UserDetailsService {

    private final UserDao userDao;

    public LightSwordUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        DashboardUser user = userDao.getUserByLoginName(s);
        if (user == null){
            throw new UsernameNotFoundException("用户 <"+ s + "> 不存在");
        }
//        List<DashboardRole> currentRoleList = roleDao.getCurrentRoleList(user.getUserId());
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        return new JwtUser(user, authorities);
    }
}
