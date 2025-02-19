package com.synesthesia.springoauth2.controller;

import com.synesthesia.springoauth2.dto.UserDTO;
import com.synesthesia.springoauth2.dto.response.WebResponse;
import com.synesthesia.springoauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;
    @GetMapping("/")
    public WebResponse<UserDTO> home(@AuthenticationPrincipal Jwt jwt) {
        UserDTO user = jwt == null
                ? UserDTO.builder().username("Guest").build()
                : userService.getAuthenticatedUser(jwt);

        return WebResponse.<UserDTO>builder().data(user).build();
    }

}
