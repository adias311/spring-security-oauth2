package com.synesthesia.springoauth2.service;

import com.synesthesia.springoauth2.dto.RoleDTO;
import com.synesthesia.springoauth2.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    public UserDTO getAuthenticatedUser(Jwt authentication) {

        try {

            if (authentication == null) {
                return UserDTO.builder()
                        .username("Guest")
                        .roles(new ArrayList<>())
                        .build();
            }

            List<RoleDTO> scope = authentication.getClaimAsStringList("roles")
                    .stream()
                    .map(RoleDTO::new)
                    .collect(Collectors.toList());

            return UserDTO.builder()
                    .username(authentication.getSubject())
                    .roles(scope)
                    .build();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing user data", e);
        }
    }
}
