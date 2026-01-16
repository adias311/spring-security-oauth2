package com.synesthesia.springoauth2.controller;

import com.synesthesia.springoauth2.configuration.WithMockJwtAuthentication;
import com.synesthesia.springoauth2.dto.RoleDTO;
import com.synesthesia.springoauth2.dto.UserDTO;
import com.synesthesia.springoauth2.service.UserService;
import org.hamcrest.CoreMatchers;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Nested
    @Tag("unit-test")
    class homeControllerUnitTest {

        @Test
        void home_WithoutJwt() throws Exception {

            UserDTO mockResponse = UserDTO.builder().username("Guest").roles(new ArrayList<>()).build();

            when(userService.getAuthenticatedUser(null)).thenReturn(mockResponse);

            ResultActions response = mockMvc.perform(get("/"));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.username" , CoreMatchers.is("Guest")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors" , CoreMatchers.nullValue()));

            verify(userService, times(1)).getAuthenticatedUser(null);
        }

        @Test
        @WithMockJwtAuthentication
        void home_ShouldReturnUserData() throws Exception {

            List<RoleDTO> roles = new ArrayList<>(
                List.of(
                    new RoleDTO("USER"),
                    new RoleDTO("ADMIN")
                )
            );

            UserDTO mockResponse = new UserDTO();
            mockResponse.setUsername("testuser");
            mockResponse.setRoles(roles);

            when(userService.getAuthenticatedUser(any(Jwt.class))).thenReturn(mockResponse);

            ResultActions response = mockMvc.perform(get("/"));

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.username" , CoreMatchers.is("testuser")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0].name" , CoreMatchers.is("USER")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[1].name" , CoreMatchers.is("ADMIN")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors" , CoreMatchers.nullValue()));

            verify(userService, times(1)).getAuthenticatedUser(any(Jwt.class));
        }




    }

}