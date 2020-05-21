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

import org.albireo.pojo.DashboardJob;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by yfyuan on 2017/2/17.
 */
@Component
public interface JobDao {
    int save(DashboardJob job);

    int update(DashboardJob job);

    List<DashboardJob> getJobList(String userId);

    List<DashboardJob> getJobListAdmin(String userId);

    int delete(Long jobId);

    int updateLastExecTime(Long jobId, Date date);

    int updateStatus(Long jobId, Long status, String log);

    DashboardJob getJob(Long jobId);

    long checkJobRole(String userId, Long jobId, String permissionPattern);

}
