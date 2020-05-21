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

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by zyong on 2017/5/26.
 */
public class TableMap extends HashMap<String, String> implements Serializable {

    @Override
    public String get(Object key) {
        return super.get(key.toString().replace("\"", ""));
    }
}