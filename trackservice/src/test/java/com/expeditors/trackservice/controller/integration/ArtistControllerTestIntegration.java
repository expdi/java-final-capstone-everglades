package com.expeditors.trackservice.controller.integration;

import com.expeditors.trackservice.domain.Artist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ArtistControllerTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "normaluser")
    void getAllArtistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "normaluser")
    void getArtistByIdTest() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/artist/by/id/1").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.firstName")
                .value("Jennifer"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    @WithMockUser(username = "normaluser")
    void getArtistByIdTestNonExist() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/artist/by/id/1000").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "normaluser")
    void getArtistByNameTest() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/artist/by/name/{0}", "Jennifer").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$", hasSize(1)));

        actions = actions.andExpect(jsonPath("$.[0].firstName")
                .value("Jennifer"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    @WithMockUser(username = "normaluser")
    void getArtistByNameTestNonExist() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/artist/by/name/BadArtist").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(jsonPath("$", hasSize(0)));

    }


    @Test
    @WithMockUser(username = "normaluser")
    void getTrackForArtistTest() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/artist/3/tracks").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$", hasSize(3)));

        actions = actions.andExpect(jsonPath("$.[0].title")
                .value("Fortnight"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    @WithMockUser(username = "normaluser")
    void getTrackForArtistTestNonExist() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/artist/1000/tracks").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(jsonPath("$", hasSize(0)));

    }

}
