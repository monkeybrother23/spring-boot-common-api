package com.albert.common.web.result;

public enum ApiStatus implements ApiMessage {
    SUCCESS("200", "success"),

    ERROR("500", "system error");

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
