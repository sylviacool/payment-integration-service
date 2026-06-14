package com.paymentintegration.service.interfaces;

public interface WebhookService {
    void handleEvent(String eventType, String orderId);

}
