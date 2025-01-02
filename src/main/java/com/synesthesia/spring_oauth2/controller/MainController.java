package com.synesthesia.spring_oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String hello(Authentication authentication) {
        if (authentication != null)  return "Hello, " + authentication.getName() + "!";
        else return "Hello, Guest!";
    }

}
