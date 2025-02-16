package com.synesthesia.spring_oauth2.configuration.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class HandleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${auth.github.url}")
    private String authGithubUrl;

    @Value("${auth.google.url}")
    private String authGoogleUrl;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException )
    throws IOException {

        try {

            if (request.getRequestURI().startsWith("/login/oauth2/authorization/")) {
                String authUrl = determineAuthUrl(request);
                new LoginUrlAuthenticationEntryPoint(authUrl)
                        .commence(request, response, authException);
            } else {
                new CustomAuthenticationEntryPoint()
                        .commence(request, response, authException);
            }

        } catch (IllegalArgumentException | ServletException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private String determineAuthUrl(HttpServletRequest request) {
        String client = request.getParameter("client");
        if ("github".equals(client)) {
            return authGithubUrl;
        } else if ("google".equals(client)) {
            return authGoogleUrl;
        } else {
            throw new IllegalArgumentException("Invalid or missing client parameter");
        }
    }

}
