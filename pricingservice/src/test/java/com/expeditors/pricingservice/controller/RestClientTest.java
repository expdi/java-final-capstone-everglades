package com.expeditors.pricingservice.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestClientTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient restClient;

    @Test
    void getBothLimits_ShouldWorkWithSSL(){

        var result = restClient
                .get()
                .uri(URI.create("https://localhost:" + port + "/pricing"))
                .retrieve()
                .toEntity(Double.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
