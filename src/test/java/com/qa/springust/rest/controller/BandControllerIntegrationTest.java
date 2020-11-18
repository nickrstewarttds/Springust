package com.qa.springust.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.springust.global.BAND;
import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.rest.dto.BandDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(profiles = "test")
class BandControllerIntegrationTest {

    private static final MediaType JSON_FORMAT = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper mapper;

    private BandDTO map(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    @Autowired
    private ObjectMapper jsonifier;

    private final String URI = "/band";

    private final Long TEST_ID = 1L;
    private final Band TEST_BAND = new Band(TEST_ID, BAND.TMG.getName());
    private final Musician TEST_MUSICIAN = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());

    @Test
    void testCreate() throws Exception {
        TEST_BAND.setId(TEST_ID + 1);
        BandDTO expected = this.map(TEST_BAND);

        this.mvc.perform(post(URI + "/create").accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_BAND))).andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testReadOne() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_MUSICIAN);
        TEST_BAND.setMusicians(musicians);
        BandDTO expected = this.map(TEST_BAND);

        this.mvc.perform(get(URI + "/read/" + TEST_ID).accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testReadAll() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_MUSICIAN);
        TEST_BAND.setMusicians(musicians);

        List<BandDTO> bands = new ArrayList<>();
        bands.add(this.map(TEST_BAND));

        this.mvc.perform(get(URI + "/read").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(bands)));
    }

    @Test
    void testUpdate() throws Exception {
        BandDTO expected = this.map(TEST_BAND);

        this.mvc.perform(put(URI + "/update/" + TEST_ID).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_BAND))).andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testDelete() throws Exception {
        this.mvc.perform(delete(URI + "/delete/" + TEST_ID)).andExpect(status().isNoContent());
    }

}
