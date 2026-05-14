package com.paymentintegration.service.impl;


import com.paymentintegration.dto.paypal.capture.response.CaptureOrderResponse;
import com.paymentintegration.dto.paypal.createorder.request.CreateOrderRequest;
import com.paymentintegration.dto.paypal.createorder.response.CreateOrderResponse;
import com.paymentintegration.dto.paypal.createorder.response.LinkDescription;
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

    @Value("${paypal.capture-order-url}")
    private String captureOrderUrl;

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

            ObjectMapper responseMapper =
                    new ObjectMapper();

            CreateOrderResponse createOrderResponse =
                    responseMapper.readValue(
                            response.getBody(),
                            CreateOrderResponse.class
                    );


            String redirectUrl = null;

            for (LinkDescription link :
                    createOrderResponse.getLinks()) {

                if ("approve".equals(link.getRel())) {

                    redirectUrl = link.getHref();
                }
            }


            OrderRes orderRes = new OrderRes();

            orderRes.setOrderId(
                    createOrderResponse.getId()
            );

            orderRes.setPaypalStatus(
                    createOrderResponse.getStatus()
            );

            orderRes.setRedirectUrl(
                    redirectUrl
            );

            return orderRes;

        } catch (Exception e) {

            log.error("Error creating PayPal order", e);

            throw new RuntimeException(e);
        }

    }


    @Override
    public OrderRes captureOrder(String orderId) {

        try {

            log.info("Capturing PayPal order: {}", orderId);

            String accessToken =
                    tokenService.getAccessToken();

            String url =
                    captureOrderUrl +
                            "/" +
                            orderId +
                            "/capture";

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setBearerAuth(accessToken);

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            HttpRequest httpRequest =
                    HttpRequest.builder()
                            .httpMethod(HttpMethod.POST)
                            .uri(url)
                            .httpHeaders(headers)
                            .body("")
                            .build();

            ResponseEntity<String> response =
                    httpServiceEngine.makeHttpCall(
                            httpRequest
                    );

            log.info("Capture response: {}",
                    response.getBody());

            ObjectMapper objectMapper =
                    new ObjectMapper();

            CaptureOrderResponse captureResponse =
                    objectMapper.readValue(
                            response.getBody(),
                            CaptureOrderResponse.class
                    );

            OrderRes orderRes =
                    new OrderRes();

            orderRes.setOrderId(
                    captureResponse.getId()
            );

            orderRes.setPaypalStatus(
                    captureResponse.getStatus()
            );

            return orderRes;

        } catch (Exception e) {

            log.error("Error capturing PayPal order", e);

            throw new RuntimeException(e);
        }
    }

}