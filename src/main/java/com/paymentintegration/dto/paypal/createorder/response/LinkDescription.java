package com.paymentintegration.dto.paypal.createorder.response;

import lombok.Data;

@Data
public class LinkDescription {

    private String href;

    private String rel;

    private String method;
}