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

package org.albireo.util.json;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by Peter on 2017/5/6.
 */
public class JSONBuilder extends JSONObject {

    public static JSONBuilder json() {
        return new JSONBuilder();
    }
    public static JSONBuilder json(Map<String, Object> map) {
        return new JSONBuilder(map);
    }
    public static JSONBuilder json(String key, Object value) {
        return new JSONBuilder().put(key, value);
    }

    public JSONBuilder() {}

    public JSONBuilder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public JSONBuilder put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public JSONBuilder getJSONObject(String key) {
        return json(super.getJSONObject(key));
    }

    public String toString(boolean prettyFormat, int spaces) {
        String result = JSONObject.toJSONString(this, prettyFormat);
        StringBuffer space = new StringBuffer();
        if (prettyFormat) {
            for (int i = 0; i < spaces; i++) {
                space.append(" ");
            }
            return result.replaceAll("\\t", space.toString());
        }
        return result;
    }

    public String toString(boolean prettyFormat) {
        return toString(prettyFormat, 0);
    }

    @Override
    public String toString() {
        return toString(true, 2);
    }
}
