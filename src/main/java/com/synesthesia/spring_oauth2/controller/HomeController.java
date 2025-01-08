package com.synesthesia.spring_oauth2.controller;

import com.synesthesia.spring_oauth2.dto.response.HomeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<HomeResponse> hello(Authentication authentication) {

        HomeResponse homeResponse = new HomeResponse();
        homeResponse.setName("Guest");
        if (authentication != null)  {
            homeResponse.setName(authentication.getName());
            return ResponseEntity.status(200).body(homeResponse);
        };
        return ResponseEntity.status(200).body(homeResponse);
    }

}
