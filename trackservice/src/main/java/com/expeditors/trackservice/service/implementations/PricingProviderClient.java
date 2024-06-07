package com.expeditors.trackservice.service.implementations;

import com.expeditors.trackservice.service.PricingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Objects;

@Service
@Profile("!test")
public class PricingProviderClient implements PricingProvider {


    private final String env;
    private static final String PRICING_URL = "http://priceservice:10002/pricing";
    private final RestClient restClient;
    public PricingProviderClient(@Value("${credentials}")  String env) {
        this.env = env;
        this.restClient = createClientForAddress();
    }

    @Override
    public double getPrice() {

        ResponseEntity<Double> priceResponse = restClient.get()
                .retrieve()
                .toEntity(Double.class);

        var price = priceResponse.getBody();

        if(Objects.isNull(price)) {
            throw new RuntimeException("No price Retrieved");
        }

        return price;
    }

    private RestClient createClientForAddress(){
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(env.getBytes());

        return RestClient.builder()
                .baseUrl(PricingProviderClient.PRICING_URL)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", authHeader)
                .build();
    }
}
