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

package org.albireo.services.role;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.albireo.dao.MenuDao;
import org.albireo.dto.DashboardMenu;
import org.albireo.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyuan on 2016/12/21.
 */
@Repository
@Aspect
public class MenuRoleService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MenuDao menuDao;

    @Value("${admin_user_id}")
    private String adminUserId;

    @Around("execution(* org.albireo.services.MenuService.getMenuList(..))")
    public Object getMenuList(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userid = authenticationService.getCurrentUser().getUserId();
        if (userid.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            final List<Long> menuIdList = menuDao.getMenuIdByUserRole(userid);
            List<DashboardMenu> list = (List<DashboardMenu>) proceedingJoinPoint.proceed();
            return new ArrayList<DashboardMenu>(Collections2.filter(list, new Predicate<DashboardMenu>() {
                @Override
                public boolean apply(@Nullable DashboardMenu dashboardMenu) {
                    return menuIdList.contains(dashboardMenu.getMenuId());
                }
            }));
        }
    }

}
