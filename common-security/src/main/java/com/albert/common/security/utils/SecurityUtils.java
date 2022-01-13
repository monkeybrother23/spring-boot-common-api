package com.albert.common.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Albert
 */
public class SecurityUtils {
    private SecurityUtils() {
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUserName() {
        //getAuthentication().getPrincipal().toString()
        Authentication authentication = getAuthentication();
        if (Objects.nonNull(authentication)) {
            return authentication.getName();
        } else {
            return "";
        }
    }

    public static List<String> getAuthorities() {
        List<String> authoritiesList = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = getAuthentication().getAuthorities();
        authorities.forEach(temp -> authoritiesList.add(temp.getAuthority()));
        return authoritiesList;
    }

    public static List<String> getAuthorities(Authentication authResult) {
        List<String> authoritiesList = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        authorities.forEach(temp -> authoritiesList.add(temp.getAuthority()));
        return authoritiesList;
    }
}
