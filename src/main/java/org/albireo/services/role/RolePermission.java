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

package org.albireo.services.role;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yfyuan on 2017/3/14.
 */
public class RolePermission {

    public static final String PATTERN_EDIT = "1_";
    public static final String PATTERN_DELETE = "_1";
    public static final String PATTERN_READ = "%";

    public static boolean isEdit(String permission) {
        return get(permission, 0);
    }

    public static boolean isDelete(String permission) {
        return get(permission, 1);
    }

    public static boolean get(String permission, int bit) {
        if (StringUtils.isEmpty(permission) || permission.length() - 1 < bit) {
            return false;
        } else {
            return permission.toCharArray()[bit] == '1';
        }
    }
}
