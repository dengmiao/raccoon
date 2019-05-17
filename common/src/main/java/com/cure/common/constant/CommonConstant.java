package com.cure.common.constant;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * @title: CommonConstant
 * @description: 通用常量
 * @author: dengmiao
 * @create: 2019-05-17 16:47
 **/
public interface CommonConstant {

    //=========================响应状态码==========================//
    /**
     * 响应状态枚举
     */
    enum HttpState {
        /**
         * OK
         */
        OK(200, "操作成功"),
        /**
         * Bad Request
         */
        BAD_REQUEST(400, "Bad Request"),
        /**
         * Unauthorized
         */
        UNAUTHORIZED(401, "Unauthorized"),
        /**
         * Payment Required
         */
        PAYMENT_REQUIRED(402, "Payment Required"),
        /**
         * Forbidden
         */
        FORBIDDEN(403, "Forbidden"),
        /**
         * Not Found
         */
        NOT_FOUND(404, "Not Found"),
        /**
         * Internal Server Error
         */
        INTERNAL_SERVER_ERROR(500, "操作失败"),
        /**
         * Open api qps request limit reached
         */
        OPEN_API_QPS_REQUEST_LIMIT_REACHED(600, "请求达到限制");

        final int value;

        final String reasonPhrase;

        HttpState (int value, String reasonPhrase) {
            this.value = value;
            this.reasonPhrase = reasonPhrase;
        }

        public int getValue() {
            return value;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this).replaceAll("\"", "")
                    + ("(value=" + this.getValue() + ", reasonPhrase=" + this.getReasonPhrase() + ")");
        }}

    //=========================实体公有属性==========================//

    /**
     * 实体公共属性
     */
    enum MetaField {
        CREATE_BY("createBy", "create_by", ""),
        CREATE_TIME("createTime", "create_time", new Date()),
        UPDATE_BY("updateBy", "update_by", ""),
        UPDATE_TIME("updateTime", "update_time", new Date()),
        DEL_FLAG("delFlag", "del_flag", 0);

        final String prop;

        final String column;

        final Object defaultValue;

        MetaField(String prop, String column, Object defaultValue) {
            this.prop = prop;
            this.column = column;
            this.defaultValue = defaultValue;
        }

        public String getProp() {
            return prop;
        }

        public String getColumn() {
            return column;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }}

    //=========================系统日志==========================//
    /**
     * 操作类型
     */
    interface LogAction {
        /**
         * 新增
         */
        String ADD = "0";
        /**
         * 修改
         */
        String UPDATE = "1";
        /**
         * 添加或修改
         */
        String ADD_UPDATE = "2";
        /**
         * 删除
         */
        String DEL = "3";
        /**
         * 打开form
         */
        String OPEN_FORM = "4";
        /**
         * 查看form
         */
        String VIEW_FORM = "5";
        /**
         * form 添加或查看
         */
        String OPEN_VIEW_FORM = "6";
        /**
         * 查询list
         */
        String SELECT_LIST = "7";

        /**
         * 查询PAGE
         */
        String SELECT_PAGE = "8";
    }

    /**
     * 日志类别
     */
    interface LogType {
        /**
         * 登录/操作/调度任务
         */
        int LOGIN = 0, OPERATE = 1, scheduled = 2;
    }

    /**
     * 日志操作人类别
     */
    interface OperatorType {
        /**
         * 后台用户/手机用户/其他
         */
        int MANAGE = 0, MOBILE = 1, OTHER = 2;
    }
}
