package com.expeditors.trackservice.controller.integration;

import com.expeditors.trackservice.domain.Artist;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ArtistControllerIntegrationTest {

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

    @Test
    @WithMockUser(username = "normaluser")
    public void testAddArtistGood() throws Exception {

        Artist artist = new Artist("Test", "test", Collections.emptySet());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(artist);

        ResultActions actions = mockMvc.perform(post("/artist").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.firstName")
                .value("Test"));

        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);
    }

    @Test
    //No User here, should get 401 Unauthorized
    public void testAddArtistUnAuthorized() throws Exception {
        Artist artist = new Artist("Test", "test", Collections.emptySet());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(artist);

        ResultActions actions = mockMvc.perform(post("/artist").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        actions = actions.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "normaluser")
    public void testUpdateArtistGood() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/artist/by/id/1").accept(MediaType.APPLICATION_JSON));
        String jsonString = actions.andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(jsonString);

        Artist artist = mapper.treeToValue(node, Artist.class);

        artist.setLastName("test update");
        String updatedJson = mapper.writeValueAsString(artist);

        ResultActions putActions = mockMvc.perform(put("/artist/{0}", artist.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson));

        putActions = putActions.andExpect(status().isNoContent());

        MvcResult result = actions.andReturn();

        ResultActions postPutActions =  mockMvc
                .perform(get("/artist/by/id/1").accept(MediaType.APPLICATION_JSON));
        String postJson = postPutActions.andReturn().getResponse().getContentAsString();

        Artist postArtist = mapper.treeToValue(mapper.readTree(postJson), Artist.class);
        assertEquals("test update", postArtist.getLastName());
    }

    @Test
    @WithMockUser(username = "normaluser")
    public void testDeleteArtistGood() throws Exception {
        ResultActions deleteActions = mockMvc.perform(delete("/artist/{0}", 2)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        deleteActions = deleteActions.andExpect(status().isNoContent());

        ResultActions postDeleteActions = mockMvc
                .perform(get("/artist/by/id/2")
                        .accept(MediaType.APPLICATION_JSON));

        postDeleteActions = postDeleteActions.andExpect(status().isNotFound());
    }
}
