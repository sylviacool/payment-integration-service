package com.paymentintegration.controller;

import com.paymentintegration.dto.request.CreateOrderReq;
import com.paymentintegration.dto.response.OrderRes;
import com.paymentintegration.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderRes> createOrder(
            @RequestBody CreateOrderReq createOrderReq
    ) {

        log.info("Create order request received: {}",
                createOrderReq);

        OrderRes response =
                orderService.createOrder(createOrderReq);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/{orderId}/capture")
    public ResponseEntity<OrderRes> captureOrder(
            @PathVariable String orderId
    ) {

        log.info("Capture order request received: {}",
                orderId);

        OrderRes response =
                orderService.captureOrder(orderId);

        return ResponseEntity.ok(response);
    }

}