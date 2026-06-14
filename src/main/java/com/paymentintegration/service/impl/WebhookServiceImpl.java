package com.paymentintegration.service.impl;

import com.paymentintegration.entity.TransactionEntity;
import com.paymentintegration.entity.TransactionStatusEntity;
import com.paymentintegration.repository.TransactionRepository;
import com.paymentintegration.repository.TransactionStatusRepository;
import com.paymentintegration.service.interfaces.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookServiceImpl implements WebhookService {

    private final TransactionRepository transactionRepository;
    private final TransactionStatusRepository transactionStatusRepository;

    @Override
    public void handleEvent(String eventType, String orderId) {

        switch (eventType) {

            case "PAYMENT.CAPTURE.COMPLETED":

                TransactionEntity transaction =
                        transactionRepository
                                .findByProviderReference(orderId)
                                .orElseThrow(() ->
                                        new RuntimeException("Transaction not found")
                                );

                TransactionStatusEntity successStatus =
                        transactionStatusRepository
                                .findByName("SUCCESS")
                                .orElseThrow(() ->
                                        new RuntimeException("Status not found")
                                );

                transaction.setTransactionStatus(successStatus);
                transactionRepository.save(transaction);
                log.info("Transaction updated to SUCCESS for order: {}", orderId);
                break;

            case "PAYMENT.CAPTURE.DENIED":

                TransactionEntity deniedTransaction =
                        transactionRepository
                                .findByProviderReference(orderId)
                                .orElseThrow(() ->
                                        new RuntimeException("Transaction not found")
                                );

                TransactionStatusEntity failedStatus =
                        transactionStatusRepository
                                .findByName("FAILED")
                                .orElseThrow(() ->
                                        new RuntimeException("Status not found")
                                );

                deniedTransaction.setTransactionStatus(failedStatus);
                transactionRepository.save(deniedTransaction);
                log.info("Transaction updated to FAILED for order: {}", orderId);
                break;

            default:
                log.info("Unhandled webhook event: {}", eventType);
        }
    }
}