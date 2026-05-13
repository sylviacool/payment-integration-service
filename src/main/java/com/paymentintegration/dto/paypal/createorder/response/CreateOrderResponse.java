package com.paymentintegration.dto.paypal.createorder.response;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderResponse {

    private String id;

    private String status;

    private List<LinkDescription> links;
}