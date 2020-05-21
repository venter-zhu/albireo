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

package org.albireo.kylin.model;

import com.alibaba.fastjson.JSONObject;
import org.albireo.kylin.KylinDataProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by zyong on 2017/12/11.
 */
public class KylinModelFactory {

    public static KylinBaseModel getKylinModel(Map<String, String> dataSource, Map<String, String> query) throws Exception {
        KylinBaseModel model = null;
        String modelName = query.get(KylinDataProvider.DATA_MODEL);
        String serverIp = dataSource.get(KylinDataProvider.SERVERIP);
        String username = dataSource.get(KylinDataProvider.USERNAME);
        String password = dataSource.get(KylinDataProvider.PASSWORD);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));
        ResponseEntity<String> restful = restTemplate.getForEntity(
                "http://" + serverIp + "/kylin/api/model/{modelName}", String.class, modelName);
        JSONObject modelJson = JSONObject.parseObject(restful.getBody());

        String kylinVersion = modelJson.getString("version");
        String[] kvs = kylinVersion.split("\\.");
        String bigVersion = kvs[0];
        if ("1".equals(bigVersion)) {
            model = new KylinModel1x(modelJson, dataSource, query, kvs);
        } else if ("2".equals(bigVersion)) {
            model = new KylinModel2x(modelJson, dataSource, query, kvs);
        }
        return model;
    }
}
