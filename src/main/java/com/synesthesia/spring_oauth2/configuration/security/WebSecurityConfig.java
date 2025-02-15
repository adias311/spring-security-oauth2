package com.synesthesia.spring_oauth2.configuration.security;

import com.synesthesia.spring_oauth2.configuration.jwt.CustomJwtGrantedAuthoritiesConverter;
import com.synesthesia.spring_oauth2.configuration.security.exception.CustomAccessDeniedHandler;
import com.synesthesia.spring_oauth2.configuration.security.exception.HandleAuthenticationEntryPoint;
import com.synesthesia.spring_oauth2.configuration.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors ->
                cors.configurationSource(request -> corsConfiguration().configure())
            )
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2Login(oauth2Login -> oauth2Login.successHandler(customOAuth2SuccessHandler()))
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter()))
            )
            .authorizeHttpRequests(
                authz -> authz
                    .requestMatchers(HttpMethod.GET , "/").permitAll()
                    .requestMatchers(HttpMethod.POST , "/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint(handleAuthenticationEntryPoint())
                    .accessDeniedHandler(new CustomAccessDeniedHandler())
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebCorsConfig corsConfiguration() {
        return new WebCorsConfig();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationConverter customJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public HandleAuthenticationEntryPoint handleAuthenticationEntryPoint() {
        return new HandleAuthenticationEntryPoint();
    }

    @Bean
    public CustomOAuth2SuccessHandler customOAuth2SuccessHandler() {
        return new CustomOAuth2SuccessHandler();
    }
}
