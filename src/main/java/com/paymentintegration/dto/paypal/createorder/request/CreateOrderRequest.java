package com.paymentintegration.dto.paypal.createorder.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {  //BE to Paypal

    private String intent;

    @JsonProperty("payment_source")
    private PaymentSource paymentSource;

    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;
}