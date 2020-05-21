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

/**
 * @author : venter.zhu
 * @version : 1.0
 * @email : zhutong@ijovo.com
 * @date :  2020/4/23 0023 23:45
 **/
public enum ResultCodeEnum {

    /**
     * 处理成功
     */
    OK(200, "处理成功"),
    /**
     * 用户名或密码失败
     */
    FAILURE(201, "用户名或密码失败"),
    /**
     * 请求参数有误
     */
    BAD_REQUEST(400, "请求参数有误"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    TOKEN_ERROR(400, "token不存在或者已经失效!"),
    /**
     * 操作失败
     */
    OPERATION_FAILED(409, "操作失败"),
    /**
     * 商品不存在
     */
    NO_PRODUCT(410, "商品不存在"),
    /**
     * 基金规则不存在
     */
    NO_FUND_RULE(411, "基金规则不存在"),
    /**
     * 基金规则已存在
     */
    EXIST_FUND_RULE(412, "基金规则已存在"),
    /**
     * 竞品大类已存在
     */
    EXIST_COMPETING_CLASS(413, "竞品大类已存在"),
    /**
     * 已存在其他供货商
     */
    EXIST_AUTH(416, "已存在其他供货商"),
    /**
     * 数据不存在
     */
    NO_DATA(414, "数据不存在"),
    /**
     * 获取供货商失败
     */
    NO_CHANNEL(415, "获取供货商失败"),
    /**
     * 数据已存在
     */
    EXIST_DATA(420, "数据已存在"),

    REPEAT_DATA(421, "请勿重复提交"),
    /**
     * 目标数据异常
     */
    EXCEPTION_DATA(421, "目标数据异常"),
    /**
     * /**
     * 缺少接口中必填参数
     */
    PARAMS_MISS(483, "缺少接口中必填参数"),
    /**
     * 参数非法
     */
    PARAM_ERROR(484, "参数非法"),
    /**
     * 不允许上班打卡
     */
    NOT_ALLOW(484, "夜深了，上班还早，当前时间段不能打上班卡"),

    /**
     * 打卡过于频繁
     */
    FREQUENTLY(484, "打卡过于频繁，五分钟后重试！"),

    /**
     * 不能删除自己
     */
    FAILED_DEL_OWN(485, "不能删除自己"),
    /**
     * 该用户已存在
     */
    FAILED_USER_ALREADY_EXIST(486, "该用户已存在"),
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    /**
     * 业务异常
     */
    NOT_IMPLEMENTED(490, "添加或者修改失败");

    private Integer code;
    private String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
}

