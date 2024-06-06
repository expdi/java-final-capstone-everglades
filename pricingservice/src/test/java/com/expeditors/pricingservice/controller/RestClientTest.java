package com.expeditors.pricingservice.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RestClientTest {

    @Value("${credentials}")
    private String authHeader;

    RestClient getRestClient(){

        return RestClient.builder()
                .baseUrl("https://localhost:10002/pricing")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", authHeader)
                .build();
    }

    @Test
    void getBothLimits_ShouldWorkWithSSL(){

        var restClient = getRestClient();
        var result = restClient
                .get()
                .retrieve()
                .toEntity(Double.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
