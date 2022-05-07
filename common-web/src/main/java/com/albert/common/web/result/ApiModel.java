package com.albert.common.web.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiModel<T extends Serializable> {

    private boolean status;

    private String code;

    private String msg;

    private T data;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public static <T extends Serializable> ApiModel<T> success() {
        ApiModel<T> result = new ApiModel<>();
        result.status = true;
        result.setCode(ApiStatus.SUCCESS.getCode());
        result.setMsg(ApiStatus.SUCCESS.getMsg());
        return result;
    }

    public static <T extends Serializable> ApiModel<T> success(T data) {
        ApiModel<T> result = success();
        result.setData(data);
        return result;
    }

    public static <T extends Serializable> ApiModel<T> success(T data, ApiMessage apiMessage) {
        ApiModel<T> result = new ApiModel<>();
        result.status = true;
        result.setCode(apiMessage.getCode());
        result.setMsg(apiMessage.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> ApiModel<ArrayList<T>> ok(List<T> list, ApiMessage apiMessage) {
        ApiModel<ArrayList<T>> apiResult = new ApiModel<>();
        apiResult.setStatus(true);
        apiResult.setCode(apiMessage.getCode());
        apiResult.setMsg(apiMessage.getMsg());
        if (!Objects.isNull(list) && !list.isEmpty()) {
            if (list instanceof ArrayList) {
                apiResult.setData((ArrayList<T>) list);
            }
        } else {
            apiResult.setData(new ArrayList<>(0));
        }
        return apiResult;
    }

    public static <T extends Serializable> ApiModel<T> fail() {
        ApiModel<T> result = new ApiModel<>();
        result.status = false;
        result.setCode(ApiStatus.FAIL.getCode());
        result.setMsg(ApiStatus.FAIL.getCode());
        return result;
    }

    public static <T extends Serializable> ApiModel<T> fail(T data) {
        ApiModel<T> result = fail();
        result.setData(data);
        return result;
    }

    public static <T extends Serializable> ApiModel<T> fail(T data, ApiMessage apiMessage) {
        ApiModel<T> result = new ApiModel<>();
        result.status = false;
        result.setCode(apiMessage.getCode());
        result.setMsg(apiMessage.getMsg());
        result.setData(data);
        return result;
    }
}

