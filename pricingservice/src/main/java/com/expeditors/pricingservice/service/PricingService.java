package com.expeditors.pricingservice.service;

public interface PricingService {

    public double getPrice();
    public String getBothLimits();
    public void setBothLimits(double lowerLimit, double upperLimit);
}
