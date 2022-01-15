package com.albert.common.security.config;

import com.albert.common.security.filter.AuthenticationFilter;
import com.albert.common.security.filter.OncePreAuthenticationFilter;
import com.albert.common.security.handler.AuthenticationLogout;
import com.albert.common.security.handler.Http401AuthenticationEntryPoint;
import com.albert.common.security.serivce.UserServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;

@MapperScan({"com.albert.common.security.mapper"})
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    private final String[] permitList = new String[]{
            "/login",
            "/swagger**/**",
            "/webjars/**",
            "/v3/**",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http       //允许跨域
                .cors().and()
                //关闭 csrf
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//去除默认session
                .and().authorizeRequests()
                // 不需要登录的请求
                .antMatchers(permitList).permitAll()
                // .antMatchers("/admin").hasAnyRole("ROLE_admin")
                //任何请求方式
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint())
                .and().addFilter(new AuthenticationFilter(authenticationManager(), redisTemplate))
                .addFilter(new OncePreAuthenticationFilter(authenticationManager(), redisTemplate))
                .logout()
                .logoutSuccessHandler(new AuthenticationLogout(redisTemplate))
                .permitAll();
    }

    /**
     * 跨域
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
