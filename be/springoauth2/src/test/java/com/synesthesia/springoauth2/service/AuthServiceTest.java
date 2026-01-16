package com.synesthesia.springoauth2.service;

import com.synesthesia.springoauth2.dto.request.auth.LoginUserRequest;
import com.synesthesia.springoauth2.dto.request.auth.RegisterUserRequest;
import com.synesthesia.springoauth2.dto.response.auth.LoginUserResponse;
import com.synesthesia.springoauth2.dto.response.auth.RegisterUserResponse;
import com.synesthesia.springoauth2.entity.Role;
import com.synesthesia.springoauth2.entity.User;
import com.synesthesia.springoauth2.repository.RoleRepository;
import com.synesthesia.springoauth2.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Nested
    @Tag("unit-test")
    class RegisterUserTest {

        @Test
        void testRegister_Success() {
            RegisterUserRequest request = new RegisterUserRequest("testuser", "password");

            when(userRepository.findFirstByUsername(request.getUsername())).thenReturn(Optional.empty());
            when(roleRepository.findFirstByName("USER")).thenReturn(Optional.of(Role.builder().name("USER").build()));
            when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed-password");

            RegisterUserResponse response = authService.register(request);

            assertNotNull(response);
            assertEquals("Registration successful", response.getMessage());
            assertEquals(request.getUsername(), response.getUser().getUsername());

            verify(validationService, times(1)).validate(request);
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        void testRegister_Fail_UsernameExists() {
            RegisterUserRequest request = new RegisterUserRequest("testuser", "password");
            when(userRepository.findFirstByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                    authService.register(request));

            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
            assertTrue(Objects.requireNonNull(exception.getReason()).contains("Username already exists"));

            verify(validationService, times(1)).validate(request);
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void testRegister_Fail_DatabaseError() {
            RegisterUserRequest request = new RegisterUserRequest("testuser", "password");
            when(userRepository.findFirstByUsername(request.getUsername())).thenReturn(Optional.empty());
            when(roleRepository.findFirstByName("USER")).thenThrow(new JpaSystemException(new RuntimeException("DB error")));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                    authService.register(request));

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
            assertTrue(Objects.requireNonNull(exception.getReason()).contains("Database server error"));

            verify(validationService, times(1)).validate(request);
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @Tag("unit-test")
    class LoginUserTest {

        @Test
        void testLogin_Success() {
            LoginUserRequest request = new LoginUserRequest("testuser", "password");
            Authentication mockAuth = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(mockAuth);
            when(jwtService.generateJwtAuthBasic(mockAuth)).thenReturn("mock-jwt");

            LoginUserResponse response = authService.login(request);

            assertNotNull(response);
            assertEquals("mock-jwt", response.getToken());
            assertEquals("Login successful", response.getMessage());

            verify(validationService, times(1)).validate(request);
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        void testLogin_Fail_BadCredentials() {
            LoginUserRequest request = new LoginUserRequest("testuser", "wrongpassword");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                    authService.login(request));

            assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
            assertTrue(Objects.requireNonNull(exception.getReason()).contains("Invalid username or password"));

            verify(validationService, times(1)).validate(request);
        }
    }

    @Nested
    @Tag("unit-test")
    class LoginOauth2Test {

        @Test
        void testLoginOauth2_Success_NewUser() {
            OAuth2User mockOauth2User = mock(OAuth2User.class);
            when(mockOauth2User.getAttribute("login")).thenReturn("oauthUser");

            when(userRepository.findFirstByUsername("oauthUser")).thenReturn(Optional.empty());
            when(roleRepository.findFirstByName("USER")).thenReturn(Optional.of(Role.builder().name("USER").build()));
            when(jwtService.generateJwtOauth2(any(User.class))).thenReturn("mock-oauth-jwt");

            String token = authService.loginOauth2(mockOauth2User);

            assertNotNull(token);
            assertEquals("mock-oauth-jwt", token);

            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        void testLoginOauth2_Success_ExistingUser() {
            OAuth2User mockOauth2User = mock(OAuth2User.class);
            when(mockOauth2User.getAttribute("login")).thenReturn("oauthUser");

            User existingUser = new User();
            existingUser.setUsername("oauthUser");
            existingUser.setRoles(Collections.singletonList(Role.builder().name("USER").build()));

            when(userRepository.findFirstByUsername("oauthUser")).thenReturn(Optional.of(existingUser));
            when(jwtService.generateJwtOauth2(existingUser)).thenReturn("mock-oauth-jwt");

            String token = authService.loginOauth2(mockOauth2User);

            assertNotNull(token);
            assertEquals("mock-oauth-jwt", token);

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void testLoginOauth2_Fail_DatabaseError() {
            OAuth2User mockOauth2User = mock(OAuth2User.class);
            when(mockOauth2User.getAttribute("login")).thenReturn("oauthUser");
            when(userRepository.findFirstByUsername("oauthUser")).thenThrow(new JpaSystemException(new RuntimeException("DB error")));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                    authService.loginOauth2(mockOauth2User));

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
            assertTrue(Objects.requireNonNull(exception.getReason()).contains("Database server error"));
        }
    }
}
