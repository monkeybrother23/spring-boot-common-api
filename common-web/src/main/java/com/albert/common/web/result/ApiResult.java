package com.albert.common.web.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiResult<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 成功返回
     *
     * @param apiMessage ApiMessage
     * @return ApiResult
     */
    public static <T extends Serializable> ApiResult<T> ok(ApiMessage apiMessage) {
        ApiResult<T> result = new ApiResult<>();
        result.status = true;
        result.setCode(apiMessage.getCode());
        result.setMsg(apiMessage.getMsg());
        return result;
    }

    /**
     * 成功返回
     *
     * @param data       T
     * @param apiMessage ApiMessage
     * @return ApiResult
     */
    public static <T extends Serializable> ApiResult<T> ok(T data, ApiMessage apiMessage) {
        ApiResult<T> result = new ApiResult<>();
        result.status = true;
        result.setCode(apiMessage.getCode());
        result.setMsg(apiMessage.getMsg());
        result.setData(data);
        return result;
    }

    /**
     * 成功返回
     *
     * @param list       List<T>
     * @param apiMessage ApiMessage
     * @return ApiResult
     */
    public static <T extends Serializable> ApiResult<ArrayList<T>> success(List<T> list, ApiMessage apiMessage) {
        ApiResult<ArrayList<T>> apiResult = new ApiResult<>();
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

    /**
     * 失败返回
     *
     * @param apiMessage ApiMessage
     * @return ApiResult
     */
    public static <T extends Serializable> ApiResult<T> fail(ApiMessage apiMessage) {
        ApiResult<T> result = new ApiResult<>();
        result.status = false;
        result.setCode(apiMessage.getCode());
        result.setMsg(apiMessage.getMsg());
        return result;
    }

    /**
     * 失败返回
     *
     * @param data       T
     * @param apiMessage ApiMessage
     * @return ApiResult
     */
    public static <T extends Serializable> ApiResult<T> fail(T data, ApiMessage apiMessage) {
        ApiResult<T> result = new ApiResult<>();
        result.status = false;
        result.setCode(apiMessage.getCode());
        result.setMsg(apiMessage.getMsg());
        result.setData(data);
        return result;
    }
}

