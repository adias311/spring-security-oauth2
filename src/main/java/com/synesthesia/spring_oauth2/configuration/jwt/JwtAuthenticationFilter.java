package com.synesthesia.spring_oauth2.configuration.jwt;

import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization") != null ? request.getHeader("Authorization") : "";
        String token = authorizationHeader.length() > 7 ? authorizationHeader.substring(7) : "";

        //kalo expired sama string jwt kosong
        if (authorizationHeader.startsWith("Bearer ")) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(token);
                JWSVerifier verifier = new RSASSAVerifier(publicKey);
                if (signedJWT.verify(verifier)) {
                    JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                    Date expirationDate = claims.getExpirationTime();
                    if (expirationDate != null && expirationDate.before(new Date())) {
                        log.warn("JWT token has expired");
                        bypass(true, request, response, filterChain);
                        return;
                    }
                    request.setAttribute("jwtClaims", claims);
                } else {
                    log.warn("Invalid JWT signature");
                }
            } catch (ParseException e) {
                log.error("Error parsing JWT token", e);
            } catch (Exception e) {
                log.error("Error verifying JWT token", e);
            }
        } else if (token.isEmpty()) {
            log.warn("Authorization header not found");
            bypass(true, request, response, filterChain);
        } else {
            bypass(false, request, response, filterChain);
        }

    }

    public void bypass(boolean b , HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (b) {
            JwtHttpServletRequestWrapper wrappedRequest = new JwtHttpServletRequestWrapper(request);
            wrappedRequest.addHeader("Authorization", "");
            String authorizationHeaderW = wrappedRequest.getHeader("Authorization");
            log.info("Authorization header: {}", authorizationHeaderW);
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
