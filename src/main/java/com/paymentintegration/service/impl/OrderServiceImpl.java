package com.paymentintegration.service.impl;


import com.paymentintegration.dto.paypal.capture.response.CaptureOrderResponse;
import com.paymentintegration.dto.paypal.createorder.request.CreateOrderRequest;
import com.paymentintegration.dto.paypal.createorder.response.CreateOrderResponse;
import com.paymentintegration.dto.paypal.createorder.response.LinkDescription;
import com.paymentintegration.dto.request.CreateOrderReq;
import com.paymentintegration.dto.response.OrderRes;
import com.paymentintegration.entity.ProviderEntity;
import com.paymentintegration.entity.TransactionEntity;
import com.paymentintegration.entity.TransactionStatusEntity;
import com.paymentintegration.helper.CreateOrderHelper;
import com.paymentintegration.http.HttpRequest;
import com.paymentintegration.http.HttpServiceEngine;
import com.paymentintegration.repository.ProviderRepository;
import com.paymentintegration.repository.TransactionRepository;
import com.paymentintegration.repository.TransactionStatusRepository;
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

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final TransactionRepository transactionRepository;

    private final ProviderRepository providerRepository;

    private final TransactionStatusRepository transactionStatusRepository;

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


            ProviderEntity provider =
                    providerRepository
                            .findByProviderName("PAYPAL")
                            .orElseThrow(() ->
                                    new RuntimeException("Provider not found")
                            );



            TransactionStatusEntity createdStatus =
                    transactionStatusRepository
                            .findByName("CREATED")
                            .orElseThrow(() ->
                                    new RuntimeException("Transaction status not found")
                            );

            TransactionStatusEntity pendingStatus =
                    transactionStatusRepository
                            .findByName("PENDING")
                            .orElseThrow(() ->
                                    new RuntimeException("Pending status not found")
                            );


            TransactionEntity transactionEntity =
                    new TransactionEntity();

            transactionEntity.setUserId(1L);

            transactionEntity.setAmount(
                    new BigDecimal(createOrderReq.getAmount())
            );

            transactionEntity.setCurrency(
                    createOrderReq.getCurrency()
            );

            transactionEntity.setTxnReference(
                    UUID.randomUUID().toString()
            );

            transactionEntity.setProvider(provider);

            transactionEntity.setTransactionStatus(createdStatus);

            transactionRepository.save(transactionEntity);


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


            transactionEntity.setProviderReference(
                    createOrderResponse.getId()
            );

            transactionEntity.setTransactionStatus(
                    pendingStatus
            );

            transactionRepository.save(transactionEntity);


            String redirectUrl = null;

            for (LinkDescription link :
                    createOrderResponse.getLinks()) {

                if ("approve".equals(link.getRel())
                        || "payer-action".equals(link.getRel())) {

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

            throw new RuntimeException( "Failed to create PayPal order");
        }

    }


    @Override
    public OrderRes captureOrder(String orderId) {

        TransactionEntity transactionEntity = null;

        try {

            log.info("Capturing PayPal order: {}", orderId);

            String accessToken =
                    tokenService.getAccessToken();


            TransactionStatusEntity successStatus =
                    transactionStatusRepository
                            .findByName("SUCCESS")
                            .orElseThrow(() ->
                                    new RuntimeException("SUCCESS status not found")
                            );


            transactionEntity =
                    transactionRepository
                            .findByProviderReference(orderId)
                            .orElseThrow(() ->
                                    new RuntimeException("Transaction not found")
                            );

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

            transactionEntity.setTransactionStatus(
                    successStatus
            );

            transactionRepository.save(transactionEntity);

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

            TransactionStatusEntity failedStatus =
                    transactionStatusRepository
                            .findByName("FAILED")
                            .orElseThrow(() ->
                                    new RuntimeException("FAILED status not found")
                            );

            if (transactionEntity != null) {

                transactionEntity.setTransactionStatus(
                        failedStatus
                );

                transactionEntity.setErrorMessage(
                        e.getMessage()
                );

                transactionRepository.save(transactionEntity);
            }

            throw new RuntimeException("Failed to capture PayPal order");
        }
    }

}