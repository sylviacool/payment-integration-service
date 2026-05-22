package com.paymentintegration.controller;

import com.paymentintegration.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TokenService tokenService;

    @GetMapping("/test-token")
    public String testToken() {

        return tokenService.getAccessToken();
    }

    @GetMapping("/public/health")
    public String health() {

        return "Application is running successfully";
    }
}