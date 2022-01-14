package com.albert.common.security.filter;

import com.albert.common.security.config.ConfigConstant;
import com.albert.common.security.model.UserTokenModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 登录成功后 走此类进行鉴权操作
 */
public class OncePreAuthenticationFilter extends BasicAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(OncePreAuthenticationFilter.class);

    private RedisTemplate<String, String> redisTemplate;

    public OncePreAuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate<String, String> redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 在过滤之前和之后执行的事件
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(ConfigConstant.TOKEN_HEADER);
        if (Objects.isNull(tokenHeader)) {
            logger.debug("token为空");
            chain.doFilter(request, response);
        } else {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(tokenHeader))) {
                // 已经登录 则调用下面的方法进行解析 并设置认证信息
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
                super.doFilterInternal(request, response, chain);
            } else {
                logger.debug("缓存未查询到token");
                chain.doFilter(request, response);
            }
        }
    }


    /**
     * 从token中获取用户信息并新建一个token
     *
     * @param tokenHeader 字符串形式的Token请求头
     * @return 带用户名和密码以及权限的Authentication
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String username = "";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 从Token中解密获取用户角色
        String json = redisTemplate.opsForValue().get(tokenHeader);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserTokenModel userTokenModel = objectMapper.readValue(json, UserTokenModel.class);
            username = userTokenModel.getUsername();
            userTokenModel.getGrantedAuthorityList().forEach(temp -> authorities.add(new SimpleGrantedAuthority(temp)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

}
