package com.expeditors.trackservice.controller;

import com.expeditors.trackservice.config.profiles.Profiles;

import java.util.Base64;
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
import java.util.List;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {JPA, PRICING_PROVIDER_LOCAL, "ssl"})
public class TrackControllerIntegrationTest {

   //    @Value("${credentials}")
   private String authHeader;

   @LocalServerPort
   private int port;

   @Autowired
   private RestClientSsl ssl;

   RestClient getRestClient() {
      authHeader = "Basic " + Base64.getEncoder()
            .encodeToString("normaluser:everglades_user".getBytes());
//              .encodeToString("normaluser:password".getBytes());

      return RestClient.builder()
            .baseUrl("https://localhost:" + port + "/track")
            .apply(ssl.fromBundle("track-ssl"))
            .defaultHeader("Accept", "application/json")
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Authorization", authHeader)
            .build();
   }

   @Test
   void projectAllowsSelfSignedCertificate() {

      var restClient = getRestClient();
      var response = restClient
            .get()
            .retrieve()
            .toEntity(List.class);

      assertThat(response
            .getStatusCode())
            .isEqualTo(HttpStatus.OK);
   }
}
