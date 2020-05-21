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

import org.albireo.dataprovider.config.AggConfig;
import org.albireo.dataprovider.result.AggregateResult;

/**
 * Created by yfyuan on 2017/1/13.
 */
public interface Aggregatable {

    /**
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @param columnName
     * @return
     */
    String[] queryDimVals(String columnName, AggConfig config) throws Exception;

    /**
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @return
     */
    String[] getColumn() throws Exception;

    /**
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @param ac aggregate configuration
     * @return
     */
    AggregateResult queryAggData(AggConfig ac) throws Exception;

    default String viewAggDataQuery(AggConfig ac) throws Exception {
        return "Not Support";
    }

}
