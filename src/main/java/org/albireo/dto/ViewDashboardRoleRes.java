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

package org.albireo.dto;

import org.albireo.pojo.DashboardRoleRes;
import org.albireo.services.role.RolePermission;

/**
 * Created by yfyuan on 2017/3/14.
 */
public class ViewDashboardRoleRes {

    private Long roleResId;
    private String roleId;
    private Long resId;
    private String resType;
    private boolean edit;
    private boolean delete;


    public ViewDashboardRoleRes(DashboardRoleRes dashboardRoleRes) {
        this.roleResId = dashboardRoleRes.getRoleResId();
        this.roleId = dashboardRoleRes.getRoleId();
        this.resId = dashboardRoleRes.getResId();
        this.resType = dashboardRoleRes.getResType();
        this.edit = RolePermission.isEdit(dashboardRoleRes.getPermission());
        this.delete = RolePermission.isDelete(dashboardRoleRes.getPermission());
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Long getRoleResId() {
        return roleResId;
    }

    public void setRoleResId(Long roleResId) {
        this.roleResId = roleResId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }
}
