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

package org.albireo.dao;

import org.albireo.pojo.DashboardUser;
import org.albireo.pojo.DashboardUserRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/12/2.
 */
@Component
public interface UserDao {
    int save(DashboardUser user);

    int deleteUserById(String userId);

    List<DashboardUser> getUserList();

    int update(DashboardUser user);

    int saveUserRole(List<DashboardUserRole> list);

    int deleteUserRole(Map<String, Object> param);

    List<DashboardUserRole> getUserRoleList();

    DashboardUser getUserByLoginName(String loginName);

    int saveNewUser(String userId, String user_name, String loginName);

    int updateUserPassword(String userId, String passowrd, String newPassword);

    int deleteUserRoleByRoleId(String roleId);

    int deleteUserRoles(Map<String, Object> param);

}
