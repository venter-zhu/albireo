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

import org.albireo.dataprovider.config.DimensionConfig;
import org.albireo.dataprovider.config.ValueConfig;

/**
 * Created by yfyuan on 2017/1/19.
 */
public class ColumnIndex {

    private int index;
    private String aggType;
    private String name;

    public static ColumnIndex fromDimensionConfig(final DimensionConfig dimensionConfig) {
        ColumnIndex columnIndex = new ColumnIndex();
        columnIndex.setName(dimensionConfig.getColumnName());
        return columnIndex;
    }

    public static ColumnIndex fromValueConfig(final ValueConfig valueConfig) {
        ColumnIndex columnIndex = new ColumnIndex();
        columnIndex.setName(valueConfig.getColumn());
        columnIndex.setAggType(valueConfig.getAggType());
        return columnIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAggType() {
        return aggType;
    }

    public void setAggType(String aggType) {
        this.aggType = aggType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
