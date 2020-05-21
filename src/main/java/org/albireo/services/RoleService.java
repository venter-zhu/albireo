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

package org.albireo.services;

import org.albireo.dao.RoleDao;
import org.albireo.pojo.DashboardRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yfyuan on 2016/12/14.
 */
@Service
public class RoleService {

    public static final String RES_BOARD = "board";

    @Value("${admin_user_id}")
    private String adminUserId;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AuthenticationService authenticationService;

    public List<Long> getResRole(String resType) {
        String userid = authenticationService.getCurrentUser().getUserId();
        return roleDao.getRoleResByResIds(userid, resType);
    }

    public List<DashboardRole> getCurrentRoleList(){
        String userid = authenticationService.getCurrentUser().getUserId();
        if (userid.equals(adminUserId)) {
            return roleDao.getRoleListAll();
        }
        return roleDao.getCurrentRoleList(userid);
    }
}
