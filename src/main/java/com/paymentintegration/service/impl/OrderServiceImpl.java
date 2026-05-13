package com.paymentintegration.service.impl;


import com.paymentintegration.dto.paypal.createorder.request.CreateOrderRequest;
import com.paymentintegration.dto.request.CreateOrderReq;
import com.paymentintegration.dto.response.OrderRes;
import com.paymentintegration.helper.CreateOrderHelper;
import com.paymentintegration.http.HttpRequest;
import com.paymentintegration.http.HttpServiceEngine;
import com.paymentintegration.service.TokenService;
import com.paymentintegration.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${paypal.create-order-url}")
    private String createOrderUrl;

    private final TokenService tokenService;

    private final CreateOrderHelper createOrderHelper;

    private final HttpServiceEngine httpServiceEngine;

    @Override
    public OrderRes createOrder(CreateOrderReq createOrderReq) {

        try {

            log.info("Creating PayPal order");

            String accessToken =
                    tokenService.getAccessToken();

            log.info("Access token generated");

            CreateOrderRequest paypalRequest =
                    createOrderHelper.buildCreateOrderRequest(
                            createOrderReq
                    );

            ObjectMapper objectMapper =
                    new ObjectMapper();

            String requestBody =
                    objectMapper.writeValueAsString(
                            paypalRequest
                    );

            log.info("PayPal request body: {}", requestBody);

            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(accessToken);

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            HttpRequest httpRequest =
                    HttpRequest.builder()
                            .httpMethod(HttpMethod.POST)
                            .uri(createOrderUrl)
                            .httpHeaders(headers)
                            .body(requestBody)
                            .build();

            ResponseEntity<String> response =
                    httpServiceEngine.makeHttpCall(
                            httpRequest
                    );

            log.info("PayPal create order response: {}",
                    response.getBody());

            OrderRes orderRes = new OrderRes();

            orderRes.setPaypalStatus("SUCCESS");

            return orderRes;

        } catch (Exception e) {

            log.error("Error creating PayPal order", e);

            throw new RuntimeException(e);
        }
    }
}