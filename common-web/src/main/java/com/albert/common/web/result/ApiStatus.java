package com.albert.common.web.result;

public enum ApiStatus implements ApiMessage {
    SUCCESS("200", "Success"),
    FORBIDDEN("403", "Forbidden"),
    VALIDATION("400", "Validation"),
    ERROR("500", "System Error");

    private final String code;
    private final String msg;

    ApiStatus(String code, String msg) {
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
}
