package com.synesthesia.spring_oauth2.service;

import com.synesthesia.spring_oauth2.entity.Role;
import com.synesthesia.spring_oauth2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class JwtService {

    @Autowired
    private JwtEncoder jwtEncoder;

    Instant now = Instant.now();

    public String createJwtAuthBasic(Authentication authentication) {
        String username = authentication.getName();
        String[] roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        return setClaimsJwt(username, roles);
    }

    public String createJwtOauth2Github(User user) {
        String username = user.getUsername();
        String[] roles = user.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);
        return setClaimsJwt(username, roles);
    }

    public  String setClaimsJwt(String username, String[] roles) {
        try {
            int expiry = 2160000;
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(username)
                    .claim("roles", roles)
                    .claim("id", "xxx")
                    .build();
            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
