package com.albert.common.web.exception;




import com.albert.common.web.result.ApiMessage;

import java.util.StringJoiner;

public class ApiException extends RuntimeException implements ApiMessage {
    private static final long serialVersionUID = 1L;

    private final String code;

    private final String msg;

    public ApiException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ApiException.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("msg='" + msg + "'")
                .toString();
    }
}
