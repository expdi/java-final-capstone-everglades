package com.expeditors.trackservice.controller;

import com.expeditors.trackservice.config.profiles.Profiles;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Base64;
import java.util.List;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {JPA, PRICING_PROVIDER_LOCAL, SSL})
public class TrackControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient restClient;

    @Test
    void projectAllowsSelfSignedCertificate(){

        var response = restClient
                .get()
                .uri(URI.create("https://localhost:" + port + "/track"))
                .retrieve()
                .toEntity(List.class);

        assertThat(response
                .getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }
}
