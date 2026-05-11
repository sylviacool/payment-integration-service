package com.paymentintegration.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpServiceEngine {

    private final RestClient restClient;

    public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {

        log.info("Making HTTP call to: {}", httpRequest.getUri());

        try {

            ResponseEntity<String> response = restClient
                    .method(httpRequest.getHttpMethod())
                    .uri(httpRequest.getUri())
                    .headers(headers ->
                            headers.addAll(httpRequest.getHttpHeaders()))
                    .body(httpRequest.getBody())
                    .retrieve()
                    .toEntity(String.class);

            log.info("HTTP call successful. Status: {}", response.getStatusCode());

            return response;

        } catch (Exception e) {

            log.error("Error during HTTP call", e);

            throw new RuntimeException("HTTP communication failed", e);
        }
    }
}