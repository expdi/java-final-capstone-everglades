package com.expeditors.trackservice.controller.integration;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.dto.TrackRequest;
import com.expeditors.trackservice.dto.TrackResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
public class TrackControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser(username = "normaluser")
    void getAllTracksTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/track"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "normaluser")
    void getTrackByIdTest() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/track/by/id/1").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.title")
                .value("Fortnight"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    @WithMockUser(username = "normaluser")
    void getTrackByIdTestNonExist() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/track/by/id/1000").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "normaluser")
    void getTrackByMediaTypeWithValuesTest() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/track/by/type/{0}", com.expeditors.trackservice.domain.MediaType.MP3).accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(username = "normaluser")
    void getTrackByMediaTypeWithNoValuesTest() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/track/by/type/{0}", com.expeditors.trackservice.domain.MediaType.OGG).accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "normaluser")
    public void testAddTrackGood() throws Exception {
        TrackRequest track = new TrackRequest("Test", "test", 2.3, com.expeditors.trackservice.domain.MediaType.MP3, LocalDate.now(), Collections.emptyList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(track);

        ResultActions actions = mockMvc.perform(post("/track").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.title")
                .value("Test"));

        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);
    }

    @Test
    //No User here, should get 401 Unauthorized
    public void testAddTrackUnAuthorized() throws Exception {
        TrackRequest track = new TrackRequest("Test", "test", 2.3, com.expeditors.trackservice.domain.MediaType.MP3, LocalDate.now(), Collections.emptyList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(track);

        ResultActions actions = mockMvc.perform(post("/track").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        actions = actions.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "normaluser")
    public void testUpdateTrackGood() throws Exception {
        TrackRequest track = new TrackRequest("Test", "test update", 2.3, com.expeditors.trackservice.domain.MediaType.MP3, LocalDate.now(), Collections.emptyList());
        ObjectMapper mapper = new ObjectMapper();
        String updatedJson = mapper.writeValueAsString(track);

        ResultActions putActions = mockMvc.perform(put("/track/{0}", 2)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson));

        putActions = putActions.andExpect(status().isNoContent());

        MvcResult result = putActions.andReturn();

        ResultActions postPutActions =  mockMvc
                .perform(get("/track/by/id/2").accept(MediaType.APPLICATION_JSON));
        String postJson = postPutActions.andReturn().getResponse().getContentAsString();

        TrackResponse postTrack = mapper.treeToValue(mapper.readTree(postJson), TrackResponse.class);
        assertEquals("test update", postTrack.getAlbum());
    }

    @Test
    @WithMockUser(username = "normaluser")
    public void testDeleteTrackGood() throws Exception {
        ResultActions deleteActions = mockMvc.perform(delete("/track/{0}", 2)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        deleteActions = deleteActions.andExpect(status().isNoContent());

        ResultActions postDeleteActions = mockMvc
                .perform(get("/track/by/id/2")
                        .accept(MediaType.APPLICATION_JSON));

        postDeleteActions = postDeleteActions.andExpect(status().isNotFound());
    }
}