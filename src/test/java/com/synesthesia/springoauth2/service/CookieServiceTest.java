package com.synesthesia.springoauth2.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieServiceTest {

    @Mock
    private HttpServletResponse response;

    private final String cookieName = "testCookie";
    private final String cookieValue = "testValue";

    @Nested
    @Tag("unit-test")
    class CookieServiceUnitTest {

        @InjectMocks
        private CookieService cookieService;

        @Test
        void testSetCookie_Success() {
            cookieService.setCookie(response, cookieName, cookieValue);

            verify(response).addCookie(argThat(cookie ->
                    cookie.getName().equals(cookieName) &&
                            cookie.getValue().equals(cookieValue) &&
                            cookie.getPath().equals("/") &&
                            cookie.isHttpOnly() &&
                            !cookie.getSecure() &&
                            cookie.getMaxAge() == 3600 * 24 * 25
            ));
        }

        @Test
        void testDeleteCookie_Success() {
            cookieService.deleteCookie(response, cookieName);

            verify(response).addCookie(argThat(cookie ->
                    cookie.getName().equals(cookieName) &&
                            cookie.getValue() == null &&
                            cookie.getPath().equals("/") &&
                            cookie.isHttpOnly() &&
                            !cookie.getSecure() &&
                            cookie.getMaxAge() == 0
            ));
        }

    }

    @Nested
    @Tag("integration-test")
    @SpringBootTest
    class CookieServiceIntegrationTest {

        @Autowired
        private CookieService cookieService;

        @Test
        void testSetCookie_Fail_NullName() {
            Exception exception = assertThrows(ConstraintViolationException.class, () ->
                    cookieService.setCookie(response, null, cookieValue)
            );

            assertTrue(exception.getMessage().contains("setCookie.name: must not be blank"));
            verify(response, never()).addCookie(any(Cookie.class));
        }

        @Test
        void testSetCookie_Fail_NullValue() {
            Exception exception = assertThrows(ConstraintViolationException.class, () ->
                    cookieService.setCookie(response, cookieName, null)
            );

            assertTrue(exception.getMessage().contains("setCookie.value: must not be blank"));
            verify(response, never()).addCookie(any(Cookie.class));
        }

        @Test
        void testDeleteCookie_Fail_NullName() {
            Exception exception = assertThrows(ConstraintViolationException.class, () ->
                    cookieService.deleteCookie(response, null)
            );

            assertTrue(exception.getMessage().contains("deleteCookie.name: must not be blank"));
            verify(response, never()).addCookie(any(Cookie.class));
        }

    }

}