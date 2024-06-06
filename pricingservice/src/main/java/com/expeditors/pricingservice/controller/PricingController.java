package com.expeditors.pricingservice.controller;

import com.expeditors.pricingservice.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping
    public ResponseEntity<?> getPrice(){
        return ResponseEntity
                .ok()
                .body(pricingService.getPrice());
    }

    @GetMapping("/limits")
    public ResponseEntity<?> getBothLimits(){
        return ResponseEntity
                .ok()
                .body(pricingService.getBothLimits());
    }

    @PutMapping("/{lowerLimit}/{upperLimit}")
    public ResponseEntity<?> setBothLimits(@PathVariable("lowerLimit") double lowerLimit, @PathVariable("upperLimit") double upperLimit){
        synchronized (this){
            if (lowerLimit < upperLimit){
                pricingService.setBothLimits(lowerLimit, upperLimit);
                return ResponseEntity.noContent().build();
            }else {
                return  ResponseEntity.badRequest().body("Upper Limit can't be less than Lower Limit: "
                        + lowerLimit + ":" + upperLimit);
            }
        }
    }
}
