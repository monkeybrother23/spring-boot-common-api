package com.albert.common.security.serivce;

import com.albert.common.security.entity.UserEntity;
import com.albert.common.security.mapper.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        if (Objects.equals("NONE_PROVIDED", userName)) {
            return new User(userName, "", Collections.emptyList());
        } else {
            UserEntity entity = userRepository.findUserEntityByUsername(userName);
            if (Objects.isNull(entity)) {
                return new User(userName, "", Collections.emptyList());
            } else {
                List<HashMap<String, String>> userRoleAndAuthorityList = userRepository.findUserRoleAndAuthorityByUsername(userName);
                List<GrantedAuthority> authorities = userRoleAndAuthorityList.stream().map(temp -> new SimpleGrantedAuthority(temp.getOrDefault("security_name", ""))).collect(Collectors.toList());
                return new User(userName, entity.getPassword(), authorities);
            }
        }
    }

}
