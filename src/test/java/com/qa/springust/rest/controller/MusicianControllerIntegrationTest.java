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
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(profiles = "test")
class MusicianControllerIntegrationTest {

    private static final MediaType JSON_FORMAT = MediaType.APPLICATION_JSON;

    // spring's in-house, request-making backend - acting as our controller
    // you only need this in integration testing - no mocked service required!
    // this acts as postman would, across your whole application
    @Autowired
    private MockMvc mvc;

    // this specifically maps objects to JSON format for us
    @Autowired
    private ObjectMapper jsonifier;

    @Autowired
    private ModelMapper mapper;

    private MusicianDTO map(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    // i've set the root string for our URI to avoid magic strings
    private final String URI = "/musician";

    private final Long TEST_ID = 1L;
    private final Musician TEST_MUSICIAN = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());
    private final Band TEST_BAND = new Band(TEST_ID, BAND.TMG.getName());

    @Test
    void testCreate() throws Exception {
        TEST_MUSICIAN.setId(TEST_ID + 1);
        TEST_MUSICIAN.setBand(TEST_BAND);
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        this.mvc.perform(post(URI + "/create").accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_MUSICIAN))).andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testReadOne() throws Exception {
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        this.mvc.perform(get(URI + "/read/" + TEST_ID).accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testReadAll() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_MUSICIAN));

        this.mvc.perform(get(URI + "/read").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(musicians)));
    }

    @Test
    void testUpdate() throws Exception {
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        this.mvc.perform(put(URI + "/update/" + TEST_ID).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_MUSICIAN))).andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testDelete() throws Exception {
        this.mvc.perform(delete(URI + "/delete/" + TEST_ID)).andExpect(status().isNoContent());
    }

}