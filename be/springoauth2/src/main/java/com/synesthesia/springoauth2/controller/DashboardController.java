package com.synesthesia.springoauth2.controller;

import com.synesthesia.springoauth2.dto.UserDTO;
import com.synesthesia.springoauth2.dto.response.WebResponse;
import com.synesthesia.springoauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping(
            path = "/user",
            produces = "application/json"
    )
    @PreAuthorize("hasRole('USER')")
    public WebResponse<UserDTO> user(@AuthenticationPrincipal Jwt authentication) {
        UserDTO authenticatedUser = userService.getAuthenticatedUser(authentication);
        return WebResponse.<UserDTO>builder().data(authenticatedUser).build();
    }

    @GetMapping(
            path = "/seller",
            produces = "application/json"
    )
    @PreAuthorize("hasRole('USER') and hasRole('SELLER')")
    public WebResponse<UserDTO> seller(@AuthenticationPrincipal Jwt authentication) {
        UserDTO authenticatedUser = userService.getAuthenticatedUser(authentication);
        return WebResponse.<UserDTO>builder().data(authenticatedUser).build();
    }

    @GetMapping(
            path = "/admin",
            produces = "application/json"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public WebResponse<UserDTO> admin(@AuthenticationPrincipal Jwt authentication) {
        UserDTO authenticatedUser = userService.getAuthenticatedUser(authentication);
        return WebResponse.<UserDTO>builder().data(authenticatedUser).build();
    }
}
