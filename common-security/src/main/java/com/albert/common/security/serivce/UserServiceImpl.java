package com.albert.common.security.serivce;

import com.albert.common.security.entity.UserEntity;
import com.albert.common.security.mapper.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        UserEntity entity = userRepository.findUserEntityByUsername(userName);
        List<GrantedAuthority> authorities;
        if (Objects.isNull(entity)) {
             throw new UsernameNotFoundException(userName);
        } else {
            List<HashMap<String, String>> userRoleAndAuthorityList = userRepository.findUserRoleAndAuthorityByUsername(userName);
            authorities = userRoleAndAuthorityList.stream().map(temp -> new SimpleGrantedAuthority(temp.getOrDefault("my_key", ""))).collect(Collectors.toList());
            return new User(entity.getUsername(), entity.getPassword(), authorities);
        }
    }

}
