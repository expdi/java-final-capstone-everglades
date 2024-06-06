package com.expeditors.trackservice.service.implementations;

import com.expeditors.trackservice.service.PricingProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Objects;

import static com.expeditors.trackservice.config.profiles.Profiles.PRICING_PROVIDER_CLIENT;

@Service
@Profile(PRICING_PROVIDER_CLIENT)
public class PricingProviderClient implements PricingProvider {

    private static final String PRICING_URL = "https://localhost:10002/pricing";
    private final RestClient restClient;

    public PricingProviderClient() {
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

        //Find a way to create a RestClientBuilder that Include SSL when Needed
        //Can add Apply and get SSL.FromBundle by Injecting a RestClientSSL

        String authHeader = "Basic " + Base64.getEncoder().encodeToString(System.getenv("credentials").getBytes());
        return RestClient.builder()
                .baseUrl(PricingProviderClient.PRICING_URL)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", authHeader)
                .build();
    }
}
