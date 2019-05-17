package com.cure.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.cure.common.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.reactivestreams.Publisher;

import java.io.Serializable;

/**
 * @title: Result
 * @description: rest接口返回
 * @author: dengmiao
 * @create: 2019-05-17 17:04
 **/
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 返回处理消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 返回数据对象 data
     */
    private T result;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    @JsonIgnore
    @JSONField(serialize=false)
    public static final CommonConstant.HttpState OK = CommonConstant.HttpState.OK;

    @JsonIgnore
    public static transient final CommonConstant.HttpState ERROR = CommonConstant.HttpState.INTERNAL_SERVER_ERROR;

    public void error500(String message) {
        this.message = message;
        this.code = ERROR.getValue();
        this.success = false;
    }

    public void success(String message) {
        this.message = message;
        this.code = OK.getValue();
        this.success = true;
    }

    public static Result<Object> error(String msg) {
        return error(ERROR.getValue(), msg);
    }

    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public static Result<Object> ok(String msg) {
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(OK.getValue());
        r.setMessage(msg);
        return r;
    }

    public static Result<Object> ok(Object obj) {
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(OK.getValue());
        r.setMessage(OK.getReasonPhrase());
        r.setResult(obj);
        return r;
    }

    public static Result ok(Publisher obj) {
        Result<Publisher> r = new Result<>();
        r.setSuccess(true);
        r.setCode(OK.getValue());
        r.setMessage(OK.getReasonPhrase());
        r.setResult(obj);
        return r;
    }

    public static Result<Void> ok() {
        Result<Void> r = new Result<>();
        r.setSuccess(true);
        r.setCode(OK.getValue());
        r.setMessage(OK.getReasonPhrase());
        return r;
    }
}
