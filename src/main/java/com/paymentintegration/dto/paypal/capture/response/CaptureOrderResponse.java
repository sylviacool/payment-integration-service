package com.paymentintegration.dto.paypal.capture.response;

import lombok.Data;

@Data
public class CaptureOrderResponse {

    private String id;

    private String status;
}