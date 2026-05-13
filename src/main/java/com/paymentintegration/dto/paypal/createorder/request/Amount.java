package com.paymentintegration.dto.paypal.createorder.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Amount {

    @JsonProperty("currency_code")
    private String currencyCode;

    private String value;
}