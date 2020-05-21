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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.albireo.dao.DatasetDao;
import org.albireo.pojo.DashboardDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yfyuan on 2016/10/11.
 */
@Service
public class DatasetService {

    @Autowired
    private DatasetDao datasetDao;

    public ServiceStatus save(String userId, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        DashboardDataset dataset = new DashboardDataset();
        dataset.setUserId(userId);
        dataset.setName(jsonObject.getString("name"));
        dataset.setData(jsonObject.getString("data"));
        dataset.setCategoryName(jsonObject.getString("categoryName"));
        if (StringUtils.isEmpty(dataset.getCategoryName())) {
            dataset.setCategoryName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dataset_name", dataset.getName());
        paramMap.put("user_id", dataset.getUserId());
        paramMap.put("category_name", dataset.getCategoryName());

        if (jsonObject.getLong("id") != null) { // 判断前端是否传递id，如果有id，那么执行更新操作
            return update(userId, json); // 如果判断数据库中有同名的数据集就执行更新操作
        } else { // 否则执行插入操作
            if (datasetDao.countExistDatasetName(paramMap) <= 0) {
                datasetDao.save(dataset);
                return new ServiceStatus(ServiceStatus.Status.Success, "success", dataset.getId()); // 把保存后生成的主键信息通过ServiceStatus传回给前台
            } else {
                return new ServiceStatus(ServiceStatus.Status.Fail, "Duplicated name");
            }
        }

    }

    public ServiceStatus update(String userId, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        DashboardDataset dataset = new DashboardDataset();
        dataset.setUserId(userId);
        dataset.setId(jsonObject.getLong("id"));
        dataset.setName(jsonObject.getString("name"));
        dataset.setCategoryName(jsonObject.getString("categoryName"));
        dataset.setData(jsonObject.getString("data"));
        dataset.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        if (StringUtils.isEmpty(dataset.getCategoryName())) {
            dataset.setCategoryName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dataset_name", dataset.getName());
        paramMap.put("user_id", dataset.getUserId());
        paramMap.put("dataset_id", dataset.getId());
        paramMap.put("category_name", dataset.getCategoryName());
        if (datasetDao.countExistDatasetName(paramMap) <= 0) {
            datasetDao.update(dataset);
            return new ServiceStatus(ServiceStatus.Status.Success, "success");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "Duplicated name");
        }
    }

    public ServiceStatus delete(String userId, Long id) {
        datasetDao.delete(id, userId);
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }
}
