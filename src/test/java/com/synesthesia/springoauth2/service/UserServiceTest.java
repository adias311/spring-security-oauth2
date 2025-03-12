package com.synesthesia.springoauth2.service;

import com.synesthesia.springoauth2.dto.UserDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.mockito.Mock;import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private Jwt jwt;

    @InjectMocks
    private UserService userService;

    @Nested
    @Tag("unit-test")
    class UserServiceUnitTest {
        @Test
        void testGetAuthenticatedUser_Success() {
            when(jwt.getSubject()).thenReturn("testuser");
            when(jwt.getClaimAsStringList("roles")).thenReturn(List.of("ROLE_USER", "ROLE_ADMIN"));

            UserDTO result = userService.getAuthenticatedUser(jwt);

            assertNotNull(result);
            assertEquals("testuser", result.getUsername());
            assertEquals(2, result.getRoles().size());
            assertEquals("ROLE_USER", result.getRoles().get(0).getName());
            assertEquals("ROLE_ADMIN", result.getRoles().get(1).getName());

            verify(jwt, times(1)).getSubject();
            verify(jwt, times(1)).getClaimAsStringList("roles");
        }

        @Test
        void testGetAuthenticatedUser_ExceptionHandling() {
            when(jwt.getClaimAsStringList("roles")).thenThrow(new RuntimeException("JWT claim error"));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> userService.getAuthenticatedUser(jwt)
            );

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
            assertEquals("Error processing user data", exception.getReason());
        }
    }

    @Nested
    @Tag("integration-test")
    class UserServiceIntegrationTest {

    }

}
