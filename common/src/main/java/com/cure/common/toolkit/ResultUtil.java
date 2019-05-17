package com.cure.common.toolkit;

import com.cure.common.constant.CommonConstant;
import com.cure.common.vo.Result;

/**
 * @program: demo
 * @description:
 * @author: dengmiao
 * @create: 2019-04-07 15:04
 **/
public class ResultUtil<T> {

    private Result<T> result;

    public static final CommonConstant.HttpState OK = CommonConstant.HttpState.OK;

    public static final CommonConstant.HttpState ERROR = CommonConstant.HttpState.INTERNAL_SERVER_ERROR;

    public ResultUtil(){
        result=new Result<>();
        result.setSuccess(true);
        result.setMessage(OK.getReasonPhrase());
        result.setCode(OK.getValue());
    }

    public Result<T> setData(T t){
        this.result.setResult(t);
        this.result.setCode(OK.getValue());
        return this.result;
    }

    public Result<T> setSuccessMsg(String msg){
        this.result.setSuccess(true);
        this.result.setMessage(msg);
        this.result.setCode(OK.getValue());
        this.result.setResult(null);
        return this.result;
    }

    public Result<T> setData(T t, String msg){
        this.result.setResult(t);
        this.result.setCode(OK.getValue());
        this.result.setMessage(msg);
        return this.result;
    }

    public Result<T> setErrorMsg(String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(ERROR.getValue());
        return this.result;
    }

    public Result<T> setErrorMsg(Integer code, String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(code);
        return this.result;
    }
}
