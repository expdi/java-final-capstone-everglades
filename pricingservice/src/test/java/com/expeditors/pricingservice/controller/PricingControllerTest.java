package com.expeditors.pricingservice.controller;

import com.expeditors.pricingservice.service.PricingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PricingControllerTest {

    @Mock
    PricingService pricingService;

    @InjectMocks
    PricingController controller;


    @Test
    void getPrice_ReturnsOKAndPrice(){

        Mockito.doReturn(2.0)
                .when(pricingService).getPrice();

        var priceResponse = controller.getPrice();

        assertEquals(priceResponse.getStatusCode(), HttpStatus.OK);
        assertInstanceOf(Double.class, priceResponse.getBody());
        assertEquals(2.0, priceResponse.getBody());
    }


    @Test
    void getBothLimits_ReturnsOKAndLimits(){
        Mockito.doReturn("Lower limit: 1.0, Upper Limit: 5.0").when(pricingService).getBothLimits();

        var limitsResponse = controller.getBothLimits();

        assertEquals(limitsResponse.getStatusCode(), HttpStatus.OK);
        assertInstanceOf(String.class, limitsResponse.getBody());
        assertEquals("Lower limit: 1.0, Upper Limit: 5.0", limitsResponse.getBody());
    }





}
