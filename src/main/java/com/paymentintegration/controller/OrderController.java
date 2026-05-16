package com.paymentintegration.controller;

import com.paymentintegration.dto.request.CreateOrderReq;
import com.paymentintegration.dto.response.OrderRes;
import com.paymentintegration.service.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

    @Operation(
            summary = "Create PayPal order",
            description = "Creates a PayPal payment order"
    )
    @PostMapping
    public ResponseEntity<OrderRes> createOrder(@Valid
            @RequestBody CreateOrderReq createOrderReq
    ) {

        log.info("Create order request received: {}",
                createOrderReq);

        OrderRes response =
                orderService.createOrder(createOrderReq);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Capture PayPal order",
            description = "Captures approved PayPal payment"
    )
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