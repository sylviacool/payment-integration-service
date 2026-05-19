package com.paymentintegration.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken("admin");
        assertNotNull(token);

        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsername() {
        String token = jwtService.generateToken("admin");
        String username = jwtService.extractUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void shouldValidateToken() {
        String token = jwtService.generateToken("admin");
        boolean isValid = jwtService.isTokenValid(token, "admin");

        assertTrue(isValid);
    }
}