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

import org.albireo.pojo.DashboardWidget;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/122.
 */
@Component
public interface WidgetDao {

    List<String> getCategoryList();

    List<DashboardWidget> getAllWidgetList();

    List<DashboardWidget> getWidgetList(String userId);

    List<DashboardWidget> getWidgetListAdmin(String userId);

    int save(DashboardWidget dashboardWidget);

    long countExistWidgetName(Map<String, Object> map);

    int update(DashboardWidget dashboardWidget);

    int delete(Long id, String userId);

    DashboardWidget getWidget(Long id);

    long checkWidgetRole(String userId, Long widgetId, String permissionPattern);
}
