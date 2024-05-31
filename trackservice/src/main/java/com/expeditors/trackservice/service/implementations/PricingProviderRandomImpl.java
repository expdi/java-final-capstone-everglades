package com.expeditors.trackservice.service.implementations;

import com.expeditors.trackservice.service.PricingProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Profile("test")
public class PricingProviderRandomImpl
        implements PricingProvider {

    @Override
    public double getPrice() {
        return ThreadLocalRandom.current().nextDouble(1, 20);
    }
}
