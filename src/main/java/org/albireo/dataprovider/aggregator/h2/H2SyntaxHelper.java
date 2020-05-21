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

package org.albireo.dataprovider.aggregator.h2;

import org.albireo.dataprovider.config.DimensionConfig;
import org.albireo.dataprovider.config.ValueConfig;
import org.albireo.dataprovider.util.SqlHelper;
import org.albireo.dataprovider.util.SqlSyntaxHelper;

/**
 * Created by zyong on 2017/9/18.
 */
public class H2SyntaxHelper extends SqlSyntaxHelper {

    @Override
    public String getProjectStr(DimensionConfig config) {
        return SqlHelper.surround(super.getProjectStr(config), "`");
    }

    @Override
    public String getColumnNameInFilter(DimensionConfig config) {
        /**
         * LPAD is a temporary workaround for number compare
         */
        return "LPAD(" + this.getProjectStr(config) + ", 20, '0')";
    }

    @Override
    public String getDimMemberStr(DimensionConfig config, int index) {
        return "LPAD(" + super.getDimMemberStr(config, index) + ", 20, '0')";
    }

    @Override
    public String getAggStr(ValueConfig vConfig) {
        String aggExp = SqlHelper.surround(vConfig.getColumn(), "`");
        switch (vConfig.getAggType()) {
            case "sum":
                return "SUM(f_todouble(" + aggExp + "))";
            case "avg":
                return "AVG(f_todouble(" + aggExp + "))";
            case "max":
                return "MAX(f_todouble(" + aggExp + "))";
            case "min":
                return "MIN(f_todouble(" + aggExp + "))";
            case "distinct":
                return "COUNT(DISTINCT " + aggExp + ")";
            default:
                return "COUNT(" + aggExp + ")";
        }
    }
}
