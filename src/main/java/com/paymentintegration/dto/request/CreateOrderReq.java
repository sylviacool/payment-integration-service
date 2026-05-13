package com.paymentintegration.dto.request;

import lombok.Data;

@Data
public class CreateOrderReq {  //FE to BE

    private String currency;

    private String amount;

    private String returnUrl;

    private String cancelUrl;
}