package com.expeditors.trackservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingProviderIntegrationTest {

    private static final String PRICING_URL = "http://localhost:10002/pricing";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGetAll() {

        var response = testRestTemplate.getForEntity(
                URI.create(PRICING_URL), Double.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }
}
