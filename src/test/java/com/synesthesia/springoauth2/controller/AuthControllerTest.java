package com.synesthesia.springoauth2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synesthesia.springoauth2.configuration.WithMockJwtAuthentication;
import com.synesthesia.springoauth2.dto.UserDTO;
import com.synesthesia.springoauth2.dto.request.auth.LoginUserRequest;
import com.synesthesia.springoauth2.dto.request.auth.RegisterUserRequest;
import com.synesthesia.springoauth2.dto.response.auth.LoginUserResponse;
import com.synesthesia.springoauth2.dto.response.auth.RegisterUserResponse;
import com.synesthesia.springoauth2.service.AuthService;
import com.synesthesia.springoauth2.service.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private CookieService cookieService;

    @Nested
    @Tag("unit-test")
    class AuthControllerUnitTest {

        @Test
        @WithMockJwtAuthentication
        void register_ShouldReturnUserData() throws Exception {
            RegisterUserRequest request = new RegisterUserRequest("testuser", "password");
            RegisterUserResponse mockResponse = new RegisterUserResponse(UserDTO.builder().username("testuser").build(), "200");

            when(authService.register(any(RegisterUserRequest.class))).thenReturn(mockResponse);

            ResultActions response = mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.user.username" , Matchers.is("testuser")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors" , Matchers.nullValue()));

            verify(authService, times(1)).register(any(RegisterUserRequest.class));
        }

        @Test
        @WithMockJwtAuthentication
        void login_ShouldReturnTokenAndSetCookie() throws Exception {
            LoginUserRequest request = new LoginUserRequest("testuser", "password");
            LoginUserResponse mockResponse = new LoginUserResponse("mock-token" , "200");

            when(authService.login(any(LoginUserRequest.class))).thenReturn(mockResponse);

            ResultActions response = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.token", Matchers.is("mock-token")));

            verify(cookieService, times(1)).setCookie(any(HttpServletResponse.class), eq("token"), eq("mock-token"));
            verify(authService, times(1)).login(any(LoginUserRequest.class));
        }

        @Test
        @WithMockJwtAuthentication
        void logout_ShouldDeleteCookie() throws Exception {
            ResultActions response = mockMvc.perform(post("/api/auth/logout"));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.is("Logout successful")));

            verify(cookieService, times(1)).deleteCookie(any(HttpServletResponse.class), eq("token"));
        }

    }

}