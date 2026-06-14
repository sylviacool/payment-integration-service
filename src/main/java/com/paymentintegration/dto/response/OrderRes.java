package com.paymentintegration.dto.response;

import lombok.Data;

@Data
public class OrderRes {  //BE to FE

    private String orderId;

    private String paypalStatus;

    private String redirectUrl;
}