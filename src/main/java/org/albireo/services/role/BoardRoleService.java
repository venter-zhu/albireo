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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.albireo.dao.BoardDao;
import org.albireo.dao.DatasetDao;
import org.albireo.dao.DatasourceDao;
import org.albireo.dao.WidgetDao;
import org.albireo.dto.ViewDashboardBoard;
import org.albireo.pojo.DashboardDataset;
import org.albireo.services.AuthenticationService;
import org.albireo.services.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyuan on 2016/12/14.
 */
@Repository
@Aspect
public class BoardRoleService {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private WidgetDao widgetDao;

    @Autowired
    private DatasetDao datasetDao;

    @Autowired
    private DatasourceDao datasourceDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Around("execution(* org.albireo.services.BoardService.getBoardData(*))")
    public Object getBoardData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Long id = (Long) proceedingJoinPoint.getArgs()[0];
        String userid = authenticationService.getCurrentUser().getUserId();
        if (boardDao.checkBoardRole(userid, id, RolePermission.PATTERN_READ) > 0) {
            ViewDashboardBoard value = (ViewDashboardBoard) proceedingJoinPoint.proceed();
            JSONArray rows = (JSONArray) value.getLayout().get("rows");
            for (Object row : rows) {
                JSONArray widgets = ((JSONObject) row).getJSONArray("widgets");
                if (widgets == null) {
                    continue;
                }
                for (Object widget : widgets) {
                    JSONObject vw = ((JSONObject) widget).getJSONObject("widget");
                    Long widgetId = vw.getLong("id");
                    Long datasetId = vw.getJSONObject("data").getLong("datasetId");
                    Long datasourceId = vw.getJSONObject("data").getLong("datasource");
                    List<Res> roleInfo = new ArrayList<>();
                    if (widgetDao.checkWidgetRole(userid, widgetId, RolePermission.PATTERN_READ) <= 0) {
                        ((JSONObject) widget).put("hasRole", false);
                        roleInfo.add(new Res("ADMIN.WIDGET", vw.getString("categoryName") + "/" + vw.getString("name")));
                    }
                    if (datasetId != null && datasetDao.checkDatasetRole(userid, datasetId, RolePermission.PATTERN_READ) <= 0) {
                        ((JSONObject) widget).put("hasRole", false);
                        DashboardDataset ds = datasetDao.getDataset(datasetId);
                        roleInfo.add(new Res("ADMIN.DATASET", ds.getCategoryName() + "/" + ds.getName()));
                    }
                    if (datasourceId != null && datasourceDao.checkDatasourceRole(userid, datasourceId, RolePermission.PATTERN_READ) <= 0) {
                        ((JSONObject) widget).put("hasRole", false);
                        roleInfo.add(new Res("ADMIN.DATASOURCE", datasourceDao.getDatasource(datasourceId).getName()));
                    }
                    ((JSONObject) widget).put("roleInfo", roleInfo);
                }
            }
            return value;
        } else {
            return null;
        }
    }

    @Around("execution(* org.albireo.services.BoardService.update(..))")
    public Object update(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String json = (String) proceedingJoinPoint.getArgs()[1];
        JSONObject jsonObject = JSONObject.parseObject(json);
        String userid = authenticationService.getCurrentUser().getUserId();
        if (boardDao.checkBoardRole(userid, jsonObject.getLong("id"), RolePermission.PATTERN_EDIT) > 0) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "No Permission");
        }
    }

    @Around("execution(* org.albireo.services.BoardService.delete(..))")
    public Object delete(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Long id = (Long) proceedingJoinPoint.getArgs()[1];
        String userid = authenticationService.getCurrentUser().getUserId();
        if (boardDao.checkBoardRole(userid, id, RolePermission.PATTERN_DELETE) > 0) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "No Permission");
        }
    }

    private class Res {
        private String type;
        private String name;

        public Res(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
