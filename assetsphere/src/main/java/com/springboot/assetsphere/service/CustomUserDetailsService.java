package com.springboot.assetsphere.service;

import java.util.Collections;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        SimpleGrantedAuthority sga = new SimpleGrantedAuthority(user.getRole().name());

        List<GrantedAuthority> list = List.of(sga);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                list
        );
    }
}
