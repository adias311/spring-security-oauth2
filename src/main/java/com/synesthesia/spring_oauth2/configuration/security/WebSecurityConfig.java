package com.synesthesia.spring_oauth2.configuration.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;


import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey key;

    @Value("${jwt.private.key}")
    private RSAPrivateKey priv;

    @Value("${auth.github.url}")
    private String authGithubUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors ->
                cors.configurationSource(request -> corsConfiguration())
            )
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2ResourceServer(
                oauth -> oauth.jwt(withDefaults())
            )
            .oauth2Login(oauth2Login -> oauth2Login.defaultSuccessUrl("/"))
            .authorizeHttpRequests(
                authz -> authz
                    .requestMatchers(HttpMethod.GET , "/").permitAll()
                    .requestMatchers(HttpMethod.POST , "/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint(this::handleAuthenticationEntryPoint)
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5500" , "http://127.0.0.1:5500/"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        return configuration;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    private void handleAuthenticationEntryPoint(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, IOException {
        if (request.getRequestURI().startsWith("/login/oauth2/authorization/")) {
            try {
                String authUrl = determineAuthUrl(request);
                new LoginUrlAuthenticationEntryPoint(authUrl)
                        .commence(request, response, authException);
            } catch (IllegalArgumentException | ServletException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } else {
            new BearerTokenAuthenticationEntryPoint().commence(request, response, authException);
        }
    }

    private String determineAuthUrl(HttpServletRequest request) {
        String client = request.getParameter("client");

        if ("github".equals(client)) {
            return authGithubUrl;
        } else if ("google".equals(client)) {
            return authGithubUrl;
        } else {
            throw new IllegalArgumentException("Invalid or missing client parameter");
        }

    }


}
