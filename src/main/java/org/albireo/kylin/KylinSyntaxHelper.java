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

package org.albireo.kylin;

import org.apache.commons.lang3.StringUtils;
import org.albireo.dataprovider.config.DimensionConfig;
import org.albireo.dataprovider.config.ValueConfig;
import org.albireo.dataprovider.util.SqlSyntaxHelper;
import org.albireo.kylin.model.KylinBaseModel;


/**
 * Created by zyong on 2017/9/18.
 */
public class KylinSyntaxHelper extends SqlSyntaxHelper {

    private KylinBaseModel kylinModel;

    public KylinSyntaxHelper(KylinBaseModel kylinModel) {
        this.kylinModel = kylinModel;
    }

    @Override
    public String getProjectStr(DimensionConfig config) {
        return kylinModel.getColumnWithAliasPrefix(config.getColumnName());
    }

    @Override
    public String getDimMemberStr(DimensionConfig config, int index) {
        String columnType = kylinModel.getColumnType(config.getColumnName());
        if (columnType.startsWith("varchar") || columnType.startsWith("date")) {
            return "'" + config.getValues().get(index) + "'";
        } else {
            return config.getValues().get(index);
        }
    }

    @Override
    public String getAggStr(ValueConfig vConfig) {
        String columnStr = vConfig.getColumn();
        if (columnStr.indexOf(".") != -1) {
            columnStr = StringUtils.substringAfter(vConfig.getColumn(), ".");
        }
        switch (vConfig.getAggType()) {
            case "sum":
                return "SUM(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS sum_" + columnStr;
            case "avg":
                return "AVG(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS avg_" + columnStr;
            case "max":
                return "MAX(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS max_" + columnStr;
            case "min":
                return "MIN(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS min_" + columnStr;
            case "distinct":
                return "COUNT(DISTINCT " + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS count_d_" + columnStr;
            default:
                return "COUNT(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS count_" + columnStr;
        }
    }


}