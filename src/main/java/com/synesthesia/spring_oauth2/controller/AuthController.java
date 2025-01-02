package com.synesthesia.spring_oauth2.controller;

import com.synesthesia.spring_oauth2.dto.request.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    JwtEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

       try {
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           loginRequest.getUsername(),
                           loginRequest.getPassword()
                   )
           );
           SecurityContextHolder.getContext().setAuthentication(authentication);


           Instant now = Instant.now();
           long expiry = 36000L;
           String scope = authentication.getAuthorities().stream()
                   .map(GrantedAuthority::getAuthority)
                   .collect(Collectors.joining(" "));
           JwtClaimsSet claims = JwtClaimsSet.builder()
                   .issuer("self")
                   .issuedAt(now)
                   .expiresAt(now.plusSeconds(expiry))
                   .subject(authentication.getName())
                   .claim("scope", scope)
                   .claim("id", "xxx")
                   .build();
           return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }

    }

}