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

package org.albireo.dto;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * Created by zyong on 2017/9/28.
 */
public class CBoardActionLog {

    private User user;
    private String requestUrl;
    private Date actionTime = new Date();

    public CBoardActionLog() {}

    public CBoardActionLog(User user, String requestUrl) {
        this.user = user;
        this.requestUrl = requestUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss.SSS");
    }
}
