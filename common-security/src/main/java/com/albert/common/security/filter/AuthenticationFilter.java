package com.albert.common.security.filter;

import com.albert.common.security.config.ConfigConstant;
import com.albert.common.security.model.UserTokenModel;
import com.albert.common.security.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 认证过滤器
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    AuthenticationManager authenticationManager;
    RedisTemplate<String, String> redisTemplate;

    public AuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate<String, String> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter(ConfigConstant.LOGIN_USERNAME);
        String password = request.getParameter(ConfigConstant.LOGIN_PASSWORD);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
    }

    //成功
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String name = authResult.getName();
        UserTokenModel model = new UserTokenModel();
        model.setUsername(name);
        model.setGrantedAuthorityList(SecurityUtils.getAuthorities(authResult));
        redisTemplate.opsForValue().set(name, objectMapper.writeValueAsString(model));
//        redisTemplate.opsForValue().set(token, model, 30, TimeUnit.MINUTES);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("code", "200");
        map.put("msg", "登陆成功");
        writer.println(objectMapper.writeValueAsString(map));
        writer.close();
        response.setStatus(HttpStatus.OK.value());
        response.flushBuffer();
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("code", "400");
        map.put("msg", "登陆失败,用户名或者密码错误");
        writer.println(objectMapper.writeValueAsString(map));
        writer.close();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.flushBuffer();
    }
}
