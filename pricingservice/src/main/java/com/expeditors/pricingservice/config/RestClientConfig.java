package com.expeditors.pricingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Base64;


@Configuration
public class RestClientConfig {

   @Bean
   public RestClient getRestClientWithSSL(
           RestClientSsl ssl,
           @Value("${credentials}")  String authHeader){

      return RestClient.builder()
              .apply(ssl.fromBundle("pricing-ssl"))
              .defaultHeader("Accept", "application/json")
              .defaultHeader("Content-Type", "application/json")
              .defaultHeader("Authorization", getBase64EncodedAuthHeaders(authHeader))
              .build();
   }

   private String getBase64EncodedAuthHeaders(String credentials){

       return "Basic " + Base64 .getEncoder()
               .encodeToString(credentials.getBytes());
   }
}
