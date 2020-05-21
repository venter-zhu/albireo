/*
 * Copyright Albireo
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

import org.albireo.exception.AlbireoException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : venter.zhu
 * @version : 1.0
 * @email : zhutong@ijovo.com
 * @date :  2020/4/23 0023 23:43
 **/

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;

    private Integer code;

    private String msg;

    private T data;

    public static <T> Result success(Integer code, String msg, T obj) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCodeEnum.OK.getCode());
        result.setMsg(ResultCodeEnum.OK.getMsg());
        if (obj != null) {
            result.setData(obj);
        }
        return result;
    }

    public static <T> Result success(Integer code, String msg, T obj, Boolean success) {
        Result result = new Result();
        result.setSuccess(success);
        result.setCode(ResultCodeEnum.OK.getCode());
        result.setMsg(msg);
        if (obj != null) {
            result.setData(obj);
        }
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result success() {
        return Result.success(null);
    }

    public static Result failure(ResultCodeEnum resultCodeEnum) {
        return Result.failure(resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }

    public static Result failure(AlbireoException e) {
        return Result.failure(e, e.getCode(), e.getMsg());
    }

    public static Result failure(Integer code, String msg) {
        return Result.failure(null, code, msg);
    }

    public static Result failure(Throwable e, ResultCodeEnum resultCodeEnum) {
        return failure(e, resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }

    public static <T> Result success(T obj) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCodeEnum.OK.getCode());
        result.setMsg(ResultCodeEnum.OK.getMsg());
        if (obj == null) {
            // 若返回数据为null 统一返回给前端{}
            result.setData(new HashMap<>(1));
        } else {
            result.setData(obj);
        }
        return result;
    }


    public static <T> Result failure(T obj, Integer code, String msg) {
        Result result = new Result();
        result.setData(obj);
        result.setCode(code);
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static Result failure(BindingResult br) {
        if (null != br && br.hasErrors()) {
            Map<String, String> map = new HashMap(16);
            List<FieldError> list = br.getFieldErrors();
            Iterator var3 = list.iterator();
            StringBuilder s = new StringBuilder();
            while (var3.hasNext()) {
                FieldError error = (FieldError) var3.next();
                map.put(error.getField(), error.getDefaultMessage());
                s.append(error.getDefaultMessage()).append("，");
            }
            if (s.length() > 0) {
                s.deleteCharAt(s.length() - 1);
            }
            return failure(map, ResultCodeEnum.PARAM_ERROR.getCode(), s.toString());
        } else {
            return failure(ResultCodeEnum.INTERNAL_SERVER_ERROR);
        }
    }

}

