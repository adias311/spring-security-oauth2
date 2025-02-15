package com.synesthesia.spring_oauth2.controller;

import com.synesthesia.spring_oauth2.dto.request.auth.LoginUserRequest;
import com.synesthesia.spring_oauth2.dto.request.auth.RegisterUserRequest;
import com.synesthesia.spring_oauth2.dto.response.auth.LoginUserResponse;
import com.synesthesia.spring_oauth2.dto.response.auth.RegisterUserResponse;
import com.synesthesia.spring_oauth2.dto.response.WebResponse;
import com.synesthesia.spring_oauth2.service.AuthService;
import com.synesthesia.spring_oauth2.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private CookieService cookieService;

    @PostMapping(
            path = "/register",
            consumes = "application/json",
            produces = "application/json"
    )
    public WebResponse<RegisterUserResponse> register(@RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUserResponse userRegister = authService.register(registerUserRequest);
        return WebResponse.<RegisterUserResponse>builder().data(userRegister).build();
    }

    @PostMapping(
            path = "/login",
            consumes = "application/json",
            produces = "application/json"
    )
    public WebResponse<LoginUserResponse> login(@RequestBody LoginUserRequest loginUserRequest,  HttpServletResponse response) {

        LoginUserResponse userLogin = authService.login(loginUserRequest);
        cookieService.setCookie(response,"token", userLogin.getToken());

        return WebResponse.<LoginUserResponse>builder().data(userLogin).build();
    }

    @PostMapping(
            path = "/logout",
            produces = "application/json"
    )
    public WebResponse<String> logout(HttpServletResponse response) {

        cookieService.deleteCookie(response, "token");

        return WebResponse.<String>builder().build();
    }



}