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

package org.albireo.dataprovider.config;

import java.util.List;

/**
 * Created by zyong on 2017/1/9.
 */
public class AggConfig {

    private List<DimensionConfig> rows;
    private List<DimensionConfig> columns;
    private List<ConfigComponent> filters;
    private List<ValueConfig> values;

    public List<DimensionConfig> getRows() {
        return rows;
    }

    public void setRows(List<DimensionConfig> rows) {
        this.rows = rows;
    }

    public List<DimensionConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<DimensionConfig> columns) {
        this.columns = columns;
    }

    public List<ConfigComponent> getFilters() {
        return filters;
    }

    public void setFilters(List<ConfigComponent> filters) {
        this.filters = filters;
    }

    public List<ValueConfig> getValues() {
        return values;
    }

    public void setValues(List<ValueConfig> values) {
        this.values = values;
    }
}
