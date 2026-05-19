package com.paymentintegration.service.impl;

import com.paymentintegration.dto.request.AuthRequest;
import com.paymentintegration.dto.response.AuthResponse;
import com.paymentintegration.security.JwtService;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldLoginSuccessfully() {

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(jwtService.generateToken("admin"))
                .thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());

        verify(jwtService)
                .generateToken("admin");
    }
}