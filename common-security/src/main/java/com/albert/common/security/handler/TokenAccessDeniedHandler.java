package com.albert.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 权限不足
 */
public class TokenAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", false);
        map.put("code", "400");
        map.put("msg", "权限不足");
        writer.println(new ObjectMapper().writeValueAsString(map));
        writer.close();
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.flushBuffer();
    }
}
