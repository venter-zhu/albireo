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

import org.albireo.pojo.DashboardRole;
import org.albireo.pojo.DashboardRoleRes;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yfyuan on 2016/12/6.
 */
@Component
public interface RoleDao {
    int save(DashboardRole role);

    List<DashboardRole> getRoleList(String userId);

    List<DashboardRole> getCurrentRoleList(String userId);

    List<DashboardRole> getRoleListAll();

    int update(DashboardRole role);

    List<DashboardRoleRes> getRoleResList();

    int saveRoleRes(DashboardRoleRes item);

    int deleteRoleRes(String roleId);

    int deleteRoleResByResId(Long resId,String resType);

    List<Long> getRoleResByResIds(String userId, String resType);

    DashboardRole getRole(String roleId);

    int deleteRole(String roleId);

    List<DashboardRoleRes> getUserRoleResList(String userId, String resType);
}
