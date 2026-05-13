package com.paymentintegration.service.interfaces;

import com.paymentintegration.dto.request.CreateOrderReq;
import com.paymentintegration.dto.response.OrderRes;

public interface OrderService {

    OrderRes createOrder(CreateOrderReq createOrderReq);
}