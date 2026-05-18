package com.paymentintegration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/webhooks")
@Slf4j
public class WebhookController {

    @PostMapping("/paypal")
    public ResponseEntity<String> handlePaypalWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "PayPal-Transmission-Id", required = false)
            String transmissionId
    ) {

        log.info("PayPal Webhook Received");
        log.info("Transmission ID: {}", transmissionId);
        log.info("Payload: {}", payload);

        return ResponseEntity.ok("Webhook received");
    }
}