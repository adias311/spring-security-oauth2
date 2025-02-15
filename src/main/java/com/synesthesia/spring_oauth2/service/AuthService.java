package com.synesthesia.spring_oauth2.service;

import com.synesthesia.spring_oauth2.dto.RoleDTO;
import com.synesthesia.spring_oauth2.dto.UserDTO;
import com.synesthesia.spring_oauth2.dto.request.auth.LoginUserRequest;
import com.synesthesia.spring_oauth2.dto.request.auth.RegisterUserRequest;
import com.synesthesia.spring_oauth2.dto.response.auth.LoginUserResponse;
import com.synesthesia.spring_oauth2.dto.response.auth.RegisterUserResponse;
import com.synesthesia.spring_oauth2.entity.Role;
import com.synesthesia.spring_oauth2.entity.User;
import com.synesthesia.spring_oauth2.repository.RoleRepository;
import com.synesthesia.spring_oauth2.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class AuthService {

    private JwtService jwtService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ValidationService validationService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;


    public AuthService(JwtService jwtService,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       ValidationService validationService,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.validationService = validationService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {

        try {
            validationService.validate(registerUserRequest);

            if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
            }

            Role role = roleRepository.findFirstByName("USER").orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error")
            );

            User user = new User();
            user.setUsername(registerUserRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
            user.setRoles(Collections.singletonList(role));
            user.setType("AUTH_BASIC");
            userRepository.save(user);

            UserDTO userDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .roles(Collections.singletonList(new RoleDTO(user.getRoles().getFirst().getName())))
                    .build();

            return RegisterUserResponse.builder().user(userDTO).message("Registration successful").build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (JpaSystemException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database server error" + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {

        try {
            validationService.validate(loginUserRequest);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserRequest.getUsername(),
                            loginUserRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.createJwtAuthBasic(authentication);

            return LoginUserResponse.builder().token(jwt).message("Login successful").build();

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

    }

    @Transactional
    public String loginOauth2(OAuth2User authentication) {

        try {
            String username = authentication.getAttribute("login");
            User userDB = userRepository.findFirstByUsername(username)
                    .orElseGet(() -> {
                        Role role = roleRepository.findFirstByName("USER").orElseThrow(
                                () -> new RuntimeException("Internal server error"));

                        User user = new User();
                        user.setUsername(username);
                        user.setRoles(Collections.singletonList(role));
                        user.setType("OAUTH2_GITHUB");
                        userRepository.save(user);
                        return user;
                    });

            return jwtService.createJwtOauth2Github(userDB);
        } catch (JpaSystemException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database server error" + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
