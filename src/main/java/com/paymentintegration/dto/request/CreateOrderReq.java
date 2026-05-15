package com.paymentintegration.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderReq {  //FE to BE

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Amount is required")
    private String amount;

    @NotBlank(message = "Return URL is required")
    private String returnUrl;

    @NotBlank(message = "Cancel URL is required")
    private String cancelUrl;
}