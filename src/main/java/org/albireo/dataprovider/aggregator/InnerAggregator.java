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

package org.albireo.dataprovider.aggregator;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.albireo.cache.CacheManager;

import java.util.Map;

/**
 * Created by zyong on 2017/1/9.
 */
public abstract class InnerAggregator implements Aggregatable {

    protected Map<String, String> dataSource;
    protected Map<String, String> query;

//    @Autowired
//    @Qualifier("rawDataCache")
    protected CacheManager<String[][]> rawDataCache;

    public InnerAggregator() {}

    public abstract void loadData(String[][] data, long interval);

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    protected String getCacheKey() {
        return Hashing.sha256().newHasher().putString(
                JSONObject.toJSON(dataSource).toString() + JSONObject.toJSON(query).toString(),
                Charsets.UTF_8).hash().toString();
    }

    public boolean checkExist() {
        return rawDataCache.get(getCacheKey()) != null;
    }

    public void cleanExist() {
        rawDataCache.remove(getCacheKey());
    }

    public void beforeLoad(String[] header) {}
    public void loadBatch(String[] header, String[][] data) {}
    public void afterLoad(){}
}
