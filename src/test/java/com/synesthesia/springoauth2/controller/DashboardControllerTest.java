package com.synesthesia.springoauth2.controller;

import com.synesthesia.springoauth2.configuration.WithMockJwtAuthentication;
import com.synesthesia.springoauth2.dto.RoleDTO;
import com.synesthesia.springoauth2.dto.UserDTO;
import com.synesthesia.springoauth2.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Nested
    @Tag("unit-test")
    class DashboardControllerUnitTest {

        @Test
        @WithMockJwtAuthentication
        void testUserDashboard() throws Exception {

            List<RoleDTO> roles = new ArrayList<>(
                List.of(
                    new RoleDTO("USER")
                )
            );

            UserDTO mockResponse = UserDTO.builder()
                .username("testuser")
                .roles(roles)
                .build();

            when(userService.getAuthenticatedUser(any(Jwt.class))).thenReturn(mockResponse);

            ResultActions response = mockMvc.perform(get("/dashboard/user"));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.username" , Matchers.is("testuser")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0].name" , Matchers.is("USER")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors" , Matchers.nullValue()));

            verify(userService, times(1)).getAuthenticatedUser(any(Jwt.class));

        }

        @Test
        @WithMockJwtAuthentication
        void testSellerDashboard() throws Exception {

            List<RoleDTO> roles = new ArrayList<>(
                    List.of(
                            new RoleDTO("USER"),
                            new RoleDTO("SELLER")
                    )
            );

            UserDTO mockResponse = UserDTO.builder()
                    .username("testuser")
                    .roles(roles)
                    .build();

            when(userService.getAuthenticatedUser(any(Jwt.class))).thenReturn(mockResponse);

            ResultActions response = mockMvc.perform(get("/dashboard/user"));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.username" , Matchers.is("testuser")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0].name" , Matchers.is("USER")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[1].name" , Matchers.is("SELLER")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors" , Matchers.nullValue()));

            verify(userService, times(1)).getAuthenticatedUser(any(Jwt.class));

        }

        @Test
        @WithMockJwtAuthentication
        void testAdminDashboard() throws Exception {

            List<RoleDTO> roles = new ArrayList<>(
                    List.of(
                            new RoleDTO("ADMIN")
                    )
            );

            UserDTO mockResponse = UserDTO.builder()
                    .username("testuser")
                    .roles(roles)
                    .build();

            when(userService.getAuthenticatedUser(any(Jwt.class))).thenReturn(mockResponse);

            ResultActions response = mockMvc.perform(get("/dashboard/user"));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.username" , Matchers.is("testuser")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0].name" , Matchers.is("ADMIN")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors" , Matchers.nullValue()));

            verify(userService, times(1)).getAuthenticatedUser(any(Jwt.class));

        }


    }

}