package com.synesthesia.spring_oauth2.configuration.security;

import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

public class WebCorsConfig {
    public CorsConfiguration configure() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
            List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
            )
        );
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        return configuration;
    }

}
