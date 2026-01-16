package com.synesthesia.springoauth2.configuration;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;


public class WithMockJwtAuthenticationFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Jwt mockJwt = Jwt.withTokenValue("mock-token")
                .subject(annotation.username())
                .claim("roles", List.of((Object[]) annotation.roles())) // Pastikan formatnya sesuai
                .header("alg", "HS256")
                .build();

        context.setAuthentication(new JwtAuthenticationToken(mockJwt));
        return context;
    }
}
