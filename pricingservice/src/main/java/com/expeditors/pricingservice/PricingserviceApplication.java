package com.expeditors.pricingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/pricingservice.properties")
public class PricingserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingserviceApplication.class, args);
    }

}
