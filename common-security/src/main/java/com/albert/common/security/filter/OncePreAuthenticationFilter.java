package com.albert.common.security.filter;

import com.albert.common.security.model.UserTokenModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

    private final RedisTemplate<String, Object> redisTemplate;

    public OncePreAuthenticationFilter(AuthenticationManager authenticationManager, RedisTemplate<String, Object> redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 在过滤之前和之后执行的事件
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        String tokenHeader = null;
        if (webApplicationContext != null) {
            tokenHeader = request.getHeader(webApplicationContext.getEnvironment().getProperty("security.token.header"));
        }
        if (Objects.isNull(tokenHeader)) {
            chain.doFilter(request, response);
        } else {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(tokenHeader))) {
                // 已经登录 则调用下面的方法进行解析 并设置认证信息
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
                chain.doFilter(request, response);
            } else {
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
        Object o = redisTemplate.opsForValue().get(tokenHeader);
        UserTokenModel userTokenModel = (UserTokenModel) o;
        if (Objects.nonNull(userTokenModel)) {
            username = userTokenModel.getUsername();
            userTokenModel.getGrantedAuthorityList().forEach(temp -> authorities.add(new SimpleGrantedAuthority(temp)));
        }
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

}
