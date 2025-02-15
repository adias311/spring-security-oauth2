package com.synesthesia.spring_oauth2.service;

import com.synesthesia.spring_oauth2.dto.RoleDTO;
import com.synesthesia.spring_oauth2.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UserService {

    public UserDTO getAuthenticatedUser(Jwt authentication) {

        try {
            ArrayList<RoleDTO> scope = authentication.getClaimAsStringList("roles").stream()
                    .map(RoleDTO::new)
                    .collect(Collectors.toCollection(ArrayList::new));

            return UserDTO.builder()
                    .username(authentication.getSubject())
                    .roles(scope)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}
