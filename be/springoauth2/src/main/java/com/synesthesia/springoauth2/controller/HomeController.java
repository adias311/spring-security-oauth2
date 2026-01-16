package com.synesthesia.springoauth2.controller;

import com.synesthesia.springoauth2.dto.UserDTO;
import com.synesthesia.springoauth2.dto.response.WebResponse;
import com.synesthesia.springoauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;
    @GetMapping("/")
    public WebResponse<UserDTO> home(@AuthenticationPrincipal Jwt jwt) throws IOException {
        UserDTO user = userService.getAuthenticatedUser(jwt);
        return WebResponse.<UserDTO>builder().data(user).build();
    }

}
