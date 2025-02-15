package com.synesthesia.spring_oauth2.configuration.security.filter;

import com.synesthesia.spring_oauth2.configuration.jwt.JwtHttpServletRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            Cookie[] cookies = request.getCookies();
            String token = "";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }

            if (!token.isEmpty()) {
                Jwt jwt = jwtDecoder.decode(token);
                request.setAttribute("jwtClaims", jwt.getClaims());
                bypass(request, response, filterChain, "Bearer " + token);
            } else {
                log.info("Authorization not found");
                bypass(request, response, filterChain, token);
            }

        } catch (JwtException e) {
            log.info(e.getMessage());
            bypass(request, response, filterChain, "");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public void bypass(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String token) throws IOException, ServletException {
        JwtHttpServletRequestWrapper wrappedRequest = new JwtHttpServletRequestWrapper(request);
        wrappedRequest.putHeader("Authorization", token);
        filterChain.doFilter(wrappedRequest, response);
    }
}

