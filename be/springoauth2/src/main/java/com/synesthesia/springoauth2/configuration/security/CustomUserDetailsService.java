package com.synesthesia.springoauth2.configuration.security;


import com.synesthesia.springoauth2.entity.User;
import com.synesthesia.springoauth2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User authUser = userRepository.findFirstByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        if (authUser.getPassword() == null) {
            authUser.setPassword("x");
        }

        List<SimpleGrantedAuthority> authUserRoles = authUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                authUser.getUsername(),
                authUser.getPassword(),
                authUserRoles
        );

        }

    }
