package com.albert.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class AuthenticationLogout implements LogoutSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationLogout.class);
    RedisTemplate<String, Object> redisTemplate;

    public AuthenticationLogout(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        //注销
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getServletContext());
        String tokenHeader = null;
        if (webApplicationContext != null) {
            tokenHeader = httpServletRequest.getHeader(webApplicationContext.getEnvironment().getProperty("security.token.header"));
        }
        if (StringUtils.hasLength(tokenHeader) && Boolean.TRUE.equals(redisTemplate.hasKey(tokenHeader))) {
            logger.debug("清除缓存token");
            redisTemplate.delete(tokenHeader);
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("code", "200");
        map.put("msg", "注销成功");
        writer.println(new ObjectMapper().writeValueAsString(map));
        writer.close();
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.flushBuffer();
    }
}