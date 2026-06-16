package com.paymentintegration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/public/health")
    public String health() {

        return "Application is running successfully";
    }
}