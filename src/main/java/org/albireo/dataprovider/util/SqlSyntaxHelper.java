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

package org.albireo.dataprovider.util;

import org.albireo.dataprovider.config.DimensionConfig;
import org.albireo.dataprovider.config.ValueConfig;

import java.sql.Types;
import java.util.Map;

/**
 * Created by zyong on 2017/9/18.
 */
public class SqlSyntaxHelper {

    private Map<String, Integer> columnTypes;

    public String getProjectStr(DimensionConfig config) {
        return config.getColumnName();
    }

    public String getColumnNameInFilter(DimensionConfig config) {
        return this.getProjectStr(config);
    }

    public String getDimMemberStr(DimensionConfig config, int index) {
        String memberStr =  config.getValues().get(index).replaceAll("'", "\\\\'");
        switch (columnTypes.get(config.getColumnName().toUpperCase())) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NVARCHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.DATE:
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return "'" + memberStr + "'";
            default:
                return memberStr;
        }
    }

    public String getAggStr(ValueConfig vConfig) {
        String aggExp = vConfig.getColumn();
        switch (vConfig.getAggType()) {
            case "sum":
                return "SUM(" + aggExp + ")";
            case "avg":
                return "AVG(" + aggExp + ")";
            case "max":
                return "MAX(" + aggExp + ")";
            case "min":
                return "MIN(" + aggExp + ")";
            case "distinct":
                return "COUNT(DISTINCT " + aggExp + ")";
            default:
                return "COUNT(" + aggExp + ")";
        }
    }

    public SqlSyntaxHelper setColumnTypes(Map<String, Integer> columnTypes) {
        this.columnTypes = columnTypes;
        return this;
    }


}
