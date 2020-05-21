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

package org.albireo.exception;

import org.albireo.dto.ResultCodeEnum;

/**
 * @author : venter.zhu
 * @version : 1.0
 * @date :  2020/4/23 0023 23:52
 **/
public class AlbireoException extends RuntimeException {
    private Integer code;
    private String msg;

    public AlbireoException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AlbireoException(String msg) {
        super(msg);
    }

    public AlbireoException(ResultCodeEnum resultCodeEnum) {
        this(resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }

    public AlbireoException(ResultCodeEnum resultCodeEnum, String msg) {
        super(msg);
        this.code = resultCodeEnum.getCode();
        this.msg = msg;
    }

    public AlbireoException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
