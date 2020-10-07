package com.qa.springust.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.springust.dto.BandDTO;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;

@SpringBootTest
@AutoConfigureMockMvc
class BandControllerIntegrationTest {

    private static final MediaType jsonFormat = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BandRepository repo;

    @Autowired
    private ObjectMapper jsonifier;

    private Long id;
    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTO;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testBand = new Band("The Mountain Goats");
        this.testBandWithId = this.repo.saveAndFlush(this.testBand);
        this.id = this.testBandWithId.getId();
        this.bandDTO = new BandDTO(this.id, this.testBand.getName(), new ArrayList<>());
    }

    @Test
    void testCreate() throws Exception {
        this.mvc.perform(post("/band/create")
                .accept(jsonFormat)
                .contentType(jsonFormat)
                .content(this.jsonifier.writeValueAsString(this.testBand)))
            .andExpect(status().isCreated())
            .andExpect(content().json(this.jsonifier.writeValueAsString(this.bandDTO)));
    }

    @Test
    void testReadOne() throws Exception {
        this.mvc.perform(get("/band/read/" + this.id)
                .accept(jsonFormat)
                .contentType(jsonFormat))
            .andExpect(status().isOk())
            .andExpect(content().json(this.jsonifier.writeValueAsString(this.bandDTO)));
    }

    @Test
    void testReadAll() throws Exception {
        final List<Band> BANDS = new ArrayList<>();
        BANDS.add(this.testBandWithId);

        this.mvc.perform(get("/band/read")
                .accept(jsonFormat)
                .contentType(jsonFormat))
            .andExpect(status().isOk())
            .andExpect(content().json(this.jsonifier.writeValueAsString(BANDS)));
    }

    @Test
    void testUpdate() throws Exception {
        final BandDTO NEW_BAND_DTO = new BandDTO(this.id, "The Extra Glenns", new ArrayList<>());
        
        this.mvc.perform(put("/band/update/" + this.id)
                .accept(jsonFormat)
                .contentType(jsonFormat)
                .content(this.jsonifier.writeValueAsString(NEW_BAND_DTO)))
            .andExpect(status().isAccepted())
            .andExpect(content().json(this.jsonifier.writeValueAsString(NEW_BAND_DTO)));
    }
    
    @Test
    void testDelete() throws Exception {
        this.mvc
            .perform(delete("/band/delete/" + this.id)
                    .accept(jsonFormat)
                    .contentType(jsonFormat)
                    .content(this.jsonifier.writeValueAsString(this.testBand)))
            .andExpect(status().isNoContent());
    }

}
