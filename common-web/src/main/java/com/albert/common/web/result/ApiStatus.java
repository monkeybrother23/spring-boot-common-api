package com.albert.common.web.result;

public enum ApiStatus implements ApiMessage {
    SUCCESS("200", "success"),
    FAIL("400", "fail"),
    ERROR("400", "error"),
    FORBIDDEN("00020", "forbidden"),
    VALIDATION("00030", "validation"),
    NULLPOINTER_EXCEPTION("00040", "NullPointerException"),

    SAVE_SUCCESS("00050", "Save success"),
    SAVE_FAIL("00051", "Save fail"),
    DELETE_SUCCESS("00052", "Delete success"),
    DELETE_FAIL("00053", "Delete fail"),
    UPDATE_SUCCESS("00054", "Update success"),
    UPDATE_FAIL("00055", "Update fail"),
    QUERY_SUCCESS("00056", "Query success"),
    QUERY_FAIL("00057", "Query fail");
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
