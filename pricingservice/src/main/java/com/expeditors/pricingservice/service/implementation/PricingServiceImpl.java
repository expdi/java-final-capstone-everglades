package com.expeditors.pricingservice.service.implementation;

import com.expeditors.pricingservice.service.PricingService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PricingServiceImpl implements PricingService {
    @Override
    public double getPrice() {
        return ThreadLocalRandom.current().nextDouble(1, 500);
    }
}
