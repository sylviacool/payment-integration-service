package com.paymentintegration.dto.paypal.createorder.response;

import com.paymentintegration.dto.paypal.createorder.request.Paypal;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderResponse { //Paypal to BE

    private String id;

    private String status;

    private List<LinkDescription> links;
}