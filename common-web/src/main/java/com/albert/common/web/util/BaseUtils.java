package com.albert.common.web.util;

import java.util.UUID;

/**
 * Albert
 * 工具类
 */
public class BaseUtils {

    private BaseUtils() {
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
