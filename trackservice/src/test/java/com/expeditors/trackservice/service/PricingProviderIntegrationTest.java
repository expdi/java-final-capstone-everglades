package com.expeditors.trackservice.service;

import com.expeditors.trackservice.service.implementations.PricingProviderClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles( profiles = {JPA, PRICING_PROVIDER_CLIENT, SSL})
public class PricingProviderIntegrationTest {

    @Autowired
    private PricingProviderClient pricingProviderClient;

    @Test
    public void testGetAll() {

        Double response = pricingProviderClient.getPrice();
        assertEquals(Double.class, response.getClass());
    }
}
