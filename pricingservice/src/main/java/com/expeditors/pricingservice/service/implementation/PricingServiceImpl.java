package com.expeditors.pricingservice.service.implementation;

import com.expeditors.pricingservice.service.PricingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PricingServiceImpl implements PricingService {

    @Value("${lowerLimit}")
    private double lowerLimit;
    @Value("${upperLimit}")
    private double upperLimit;

    @Override
    public double getPrice() {
        DecimalFormat df = new DecimalFormat("###.##");
        var price = ThreadLocalRandom.current().nextDouble(lowerLimit, upperLimit);
        return Double.parseDouble(df.format(price));
    }

    @Override
    public String getBothLimits() {
        return "Lower limit: " + lowerLimit + ", Upper Limit: " + upperLimit;
    }

    @Override
    public void setBothLimits(double lowerLimit, double upperLimit) {
        if(lowerLimit < upperLimit){
            this.lowerLimit = lowerLimit;
            this.upperLimit = upperLimit;
        }
    }



}
