package com.expeditors.trackservice.service.implementations;

import com.expeditors.trackservice.service.PricingProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Base64;
import java.util.Objects;

import static com.expeditors.trackservice.config.profiles.Profiles.PRICING_PROVIDER_CLIENT;

@Service
@Profile(PRICING_PROVIDER_CLIENT)
public class PricingProviderClient implements PricingProvider {

    private final RestClient restClient;

    public PricingProviderClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public double getPrice() {

        ResponseEntity<Double> priceResponse = restClient
                .get()
                .uri(URI.create("https://localhost:10002/pricing"))
                .retrieve()
                .toEntity(Double.class);

        var price = priceResponse.getBody();

        if(Objects.isNull(price)) {
            throw new RuntimeException("No price Retrieved");
        }

        return price;
    }
}
