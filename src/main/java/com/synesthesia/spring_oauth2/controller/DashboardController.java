package com.synesthesia.spring_oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@PreAuthorize("hasAuthority('SCOPE_USER')")
public class DashboardController {

    @GetMapping("/user")
    public String user(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String id = jwt.getClaimAsString("id");
        return "Hello, User: " + id + "!";
    }

    @GetMapping("/seller")
    @PreAuthorize("hasAuthority('SCOPE_SELLER')")
    public String seller(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String id = jwt.getClaimAsString("id");
        return "Hello, Seller: " + id + "!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public String admin(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String id = jwt.getClaimAsString("id");
        return "Hello, Admin: " + id + "!";
    }
}
