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

package org.albireo.dataprovider.result;

import java.util.List;

/**
 * Created by yfyuan on 2017/1/18.
 */
public class AggregateResult {
    private List<ColumnIndex> columnList;
    private String[][] data;

    public AggregateResult(List<ColumnIndex> columnList, String[][] data) {
        this.columnList = columnList;
        this.data = data;
    }

    public AggregateResult() {
    }

    public List<ColumnIndex> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnIndex> columnList) {
        this.columnList = columnList;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }
}
