package com.expeditors.trackservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

import java.util.Base64;

import static com.expeditors.trackservice.config.profiles.Profiles.SSL;

@Configuration
public class RestClientConfig {

   @Bean
   @Primary
   @Profile(value = SSL)
   public RestClient getRestClientWithSSL(
           RestClientSsl ssl,
           @Value("${credentials}")  String authHeader){

      return RestClient.builder()
              .apply(ssl.fromBundle("track-ssl"))
              .defaultHeader("Accept", "application/json")
              .defaultHeader("Content-Type", "application/json")
              .defaultHeader("Authorization", getBase64EncodedAuthHeaders(authHeader))
              .build();
   }

   @Bean
   public RestClient getRestClient(@Value("${credentials}")  String authHeader){

      return RestClient.builder()
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
