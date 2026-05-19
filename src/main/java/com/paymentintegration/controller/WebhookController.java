package com.paymentintegration.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/webhooks")
@Slf4j
public class WebhookController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/paypal")
    public ResponseEntity<String> handlePaypalWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "PayPal-Transmission-Id", required = false)
            String transmissionId
    ) {

        try {
            log.info("PayPal Webhook Received");
            log.info("Transmission ID: {}", transmissionId);

            JsonNode root =
                    objectMapper.readTree(payload);

            String eventType =
                    root.get("event_type").asText();

            log.info("Event Type: {}", eventType);

            String paypalOrderId =
                    root
                            .get("resource")
                            .get("id").asText();

            log.info("PayPal Order ID: {}", paypalOrderId); //maps to provider_reference in DB

            switch (eventType) {
                case "PAYMENT.CAPTURE.COMPLETED":
                    log.info("Payment completed successfully");
                    break;

                case "PAYMENT.CAPTURE.DENIED":
                    log.info("Payment was denied");
                    break;

                default:
                    log.info("Unhandled webhook event");
            }

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception ex) {
            log.error("Webhook processing failed", ex);
            return ResponseEntity.badRequest()
                    .body("Webhook processing failed");
        }
    }
}