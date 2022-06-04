package com.albert.common.security.filter;

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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 认证过滤器
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    AuthenticationManager authenticationManager;
    RedisTemplate<String, Object> redisTemplate;

    public AuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate<String, Object> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        StringBuilder sb = new StringBuilder("");
        try (BufferedReader br = request.getReader()) {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = sb.toString();
        String username = null;
        String password = null;
        HashMap<String, String> map = new HashMap<>();
        str = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
        str = str.replace("\"", "");
        String[] split = str.split(",");
        for (String s : split) {
            String[] temp = s.split(":");
            if (temp.length == 2) {
                map.put(temp[0].trim(), temp[1].trim());
            }
        }
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        if (webApplicationContext != null) {
            String loginUsername = webApplicationContext.getEnvironment().getProperty("security.token.username");
            String loginPassword = webApplicationContext.getEnvironment().getProperty("security.token.password");
            username = map.getOrDefault(loginUsername, "NULL");
            password = map.getOrDefault(loginPassword, "NULL");
        }
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
        redisTemplate.opsForValue().set(name, model);
//        redisTemplate.opsForValue().set(token, model, 30, TimeUnit.MINUTES);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("code", "200");
        map.put("msg", "登陆成功");
        map.put("data", name);
        writer.println(objectMapper.writeValueAsString(map));
        writer.close();
        response.setStatus(HttpStatus.OK.value());
        response.flushBuffer();
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("code", "400");
        map.put("msg", "登陆失败,用户名或者密码错误");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(new ObjectMapper().writeValueAsString(map));
    }
}
