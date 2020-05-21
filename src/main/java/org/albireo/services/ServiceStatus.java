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

package org.albireo.services;

/**
 * Created by zyong on 2016/9/26.
 */
public class ServiceStatus {

    private Status status;
    private String msg;

    private Long id;

    public ServiceStatus(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServiceStatus(Status status, String msg, Long id) {
        this.status = status;
        this.msg = msg;
        this.id = id;
    }

    public enum Status {

        Success(1), Fail(2);

        private int status;

        Status(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public String toString() {
            return  new Integer(this.status).toString();
        }
    }

    public String getStatus() {
        return status.toString();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
