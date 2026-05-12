package com.paymentintegration.service;

import com.paymentintegration.constant.Constants;
import com.paymentintegration.http.HttpRequest;
import com.paymentintegration.http.HttpServiceEngine;
import com.paymentintegration.paypal.PaypalAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tools.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.oauth-url}")
    private String paypalOAuthUrl;

    private final HttpServiceEngine httpServiceEngine;

    public String getAccessToken() {

        log.info("Generating PayPal access token");

        //headers
        HttpHeaders headers = new HttpHeaders();

        headers.setBasicAuth(clientId, clientSecret);

        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );

        //body
        MultiValueMap<String, String> body =
                new LinkedMultiValueMap<>();


        body.add(
                Constants.GRANT_TYPE,
                Constants.CLIENT_CREDENTIALS
        );

        HttpRequest httpRequest = HttpRequest.builder()
                .httpMethod(HttpMethod.POST)
                .uri(paypalOAuthUrl)
                .httpHeaders(headers)
                .body(body)
                .build();

        ResponseEntity<String> response =
                httpServiceEngine.makeHttpCall(httpRequest);

        log.info("OAuth response received: {}", response.getBody());

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            PaypalAuthResponse authResponse =
                    objectMapper.readValue(
                            response.getBody(),
                            PaypalAuthResponse.class
                    );

            log.info("Access token generated successfully");

            return authResponse.getAccessToken();

        } catch (Exception e) {

            log.error("Error parsing PayPal OAuth response", e);

            throw new RuntimeException(
                    "Failed to parse PayPal OAuth response",
                    e
            );
        }
    }
}