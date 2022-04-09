package com.albert.common.web.result;

public enum ApiStatus implements ApiMessage {
    SUCCESS("00010", "success"),
    FAIL("00011", "fail"),
    ERROR("00012", "error"),

    FORBIDDEN("00020", "forbidden"),
    VALIDATION("00030", "validation"),

    NULLPOINTER_EXCEPTION("00040", "NullPointerException");
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
