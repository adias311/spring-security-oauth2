package com.synesthesia.springoauth2.configuration;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationFactory.class)
public @interface WithMockJwtAuthentication {
    String username() default "testuser";
    String[] roles() default {"USER"};
}
