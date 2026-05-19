package com.paymentintegration.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentintegration.entity.TransactionEntity;
import com.paymentintegration.entity.TransactionStatusEntity;
import com.paymentintegration.repository.TransactionRepository;
import com.paymentintegration.repository.TransactionStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/webhooks")
@Slf4j
public class WebhookController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TransactionRepository transactionRepository;
    private final TransactionStatusRepository transactionStatusRepository;

    @PostMapping("/paypal")
    public ResponseEntity<String> handlePaypalWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "PayPal-Transmission-Id", required = false)
            String transmissionId
    ) {

        try {
            log.info("PayPal Webhook Received");
            log.info("Transmission ID: {}", transmissionId);
            JsonNode root = objectMapper.readTree(payload);

            String eventType = root.get("event_type").asText();

            log.info("Event Type: {}", eventType);

            String paypalOrderId =
                    root
                            .get("resource")
                            .get("id").asText();

            log.info("PayPal Order ID: {}", paypalOrderId); //maps to provider_reference in DB

            switch (eventType) {
                case "PAYMENT.CAPTURE.COMPLETED":
                    TransactionEntity transaction = transactionRepository.findByProviderReference(paypalOrderId)
                                    .orElseThrow(() -> new RuntimeException("Transaction not found")
                                    );

                    TransactionStatusEntity completedStatus = transactionStatusRepository.findByName("SUCCESS")
                                    .orElseThrow(() -> new RuntimeException("Status not found")
                                    );

                    transaction.setTransactionStatus(completedStatus);

                    transactionRepository.save(transaction);

                    log.info(
                            "Transaction updated to SUCCESS"
                    );
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