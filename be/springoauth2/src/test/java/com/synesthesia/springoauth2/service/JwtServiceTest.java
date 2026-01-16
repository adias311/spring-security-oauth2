package com.synesthesia.springoauth2.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.mockito.Mock;import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private Authentication authentication;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private JwtService jwtService;

    @Nested
    @Tag("unit-test")
    class JwtServiceUnitTest {

        private static String username;
        private static String[] roles;
        private static Jwt mockJwt;
        private static List<SimpleGrantedAuthority> authorities;

        @BeforeAll
        static void init() {

             username = "testuser";
             roles = new String[]{"ROLE_USER", "ROLE_ADMIN"};

            JwtClaimsSet mockClaims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(2160000))
                    .subject(username)
                    .claim("roles", roles)
                    .claim("id", "xxx")
                    .build();

             mockJwt = Jwt.withTokenValue("mock-token")
                    .header("alg", "HS256")
                    .claims(claimsMap -> claimsMap.putAll(mockClaims.getClaims()))
                    .build();

             authorities = List.of(
                     new SimpleGrantedAuthority("ROLE_USER"),
                     new SimpleGrantedAuthority("ROLE_ADMIN")
                );
        }

        @Test
        void testGenerateJwtAuthBasic_Success() {

            when(authentication.getName()).thenReturn(username);
            when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

            String jwt = jwtService.generateJwtAuthBasic(authentication);

            assertNotNull(jwt, "jwt cannot be null");
            assertEquals("mock-token", jwt, "jwt should be equal to mock-token");

            verify(authentication, times(1)).getName();
            verify(authentication, times(1)).getAuthorities();
            verify(jwtEncoder).encode(argThat(parameters -> {
                JwtClaimsSet claims = parameters.getClaims();
                assertEquals(username, claims.getSubject(), "subject should be equal to username");
                assertArrayEquals(roles, claims.getClaim("roles"), "roles should be equal to roles");
                return "xxx".equals(claims.getClaim("id"));
            }));
        }

        @Test
        void testGenerateJwtAuthBasic_Fail_UsernameNull() {
            when(authentication.getName()).thenReturn(null);
            when(authentication.getAuthorities()).thenReturn(List.of());

            Exception exception = assertThrows(ResponseStatusException.class, () ->
                    jwtService.generateJwtAuthBasic(authentication)
            );

            assertTrue(exception.getMessage().contains("value cannot be null"), "Exception should mention null username");
            verify(authentication, times(1)).getName();
            verify(authentication, times(1)).getAuthorities();
            verifyNoInteractions(jwtEncoder);
        }

        @Test
        void testGenerateJwtAuthBasic_Fail_EmptyAuthorities() {
            String username = "testuser";
            when(authentication.getName()).thenReturn(username);
            when(authentication.getAuthorities()).thenReturn(List.of()); // No roles

            JwtClaimsSet mockClaims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(2160000))
                    .subject(username)
                    .claim("roles", new String[]{}) // Empty roles
                    .claim("id", "xxx")
                    .build();

            Jwt mockJwt = Jwt.withTokenValue("mock-token")
                    .header("alg", "HS256")
                    .claims(claimsMap -> claimsMap.putAll(mockClaims.getClaims()))
                    .build();

            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

            String jwt = jwtService.generateJwtAuthBasic(authentication);

            assertNotNull(jwt, "jwt cannot be null");
            assertEquals("mock-token", jwt, "jwt should be equal to mock-token");

            verify(authentication, times(1)).getName();
            verify(authentication, times(1)).getAuthorities();
            verify(jwtEncoder).encode(argThat(parameters -> {
                JwtClaimsSet claims = parameters.getClaims();
                assertEquals(username, claims.getSubject(), "subject should be equal to username");
                assertArrayEquals(new String[]{}, claims.getClaim("roles"), "roles should be empty");
                return "xxx".equals(claims.getClaim("id"));
            }));
        }

        @Test
        void testGenerateJwtAuthBasic_Fail_JwtEncodingError() {
            String username = "testuser";
            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            when(authentication.getName()).thenReturn(username);
            when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

            when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                    .thenThrow(new RuntimeException("Encoding failed"));

            Exception exception = assertThrows(RuntimeException.class, () ->
                    jwtService.generateJwtAuthBasic(authentication)
            );

            assertTrue(exception.getMessage().contains("Encoding failed"), "Exception should match expected error");

            verify(authentication, times(1)).getName();
            verify(authentication, times(1)).getAuthorities();
            verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
        }

    }

    @Nested
    @Tag("integration-test")
    class JwtServiceIntegrationTest {

    }

}