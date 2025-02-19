package com.synesthesia.springoauth2.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

public class WebCorsConfig {

    @Value("${spring.security.list-cors-allowed-origins}")
    List<String> allowedOrigins;
    public CorsConfiguration configure() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        return configuration;
    }

}
