package com.expeditors.pricingservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PricingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPrice_ReturnsOKAndPrice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pricing"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSetLimitsWithGoodInputGivesNoContent() throws Exception {
        var actions = mockMvc.perform(put("/pricing/{lowerlimit}/{upperLimit}", 15.0, 60.0))
                .andExpect(status().isNoContent()).andReturn();

         actions =  mockMvc.perform(get("/pricing/limits"))
                .andExpect(status().isOk()).andReturn();

        var bothLimits = actions.getResponse().getContentAsString();

        System.out.println("bothLimits: " + bothLimits);

        assertEquals("Lower limit: 15.0, Upper Limit: 60.0", bothLimits);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSetLimitsWithBadInputGivesBadRequest() throws Exception {
        var actions = mockMvc.perform(put("/pricing/{lowerLimit}/{upperLimit}", 65.0, 60.0))
                .andExpect(status().isBadRequest()).andReturn();

        var resultString = actions.getResponse().getContentAsString();
        System.out.println("resultString: " + resultString);
    }
}
