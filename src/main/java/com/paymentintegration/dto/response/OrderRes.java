package com.paymentintegration.dto.response;

import lombok.Data;

@Data
public class OrderRes {

    private String orderId;

    private String paypalStatus;

    private String redirectUrl;
}