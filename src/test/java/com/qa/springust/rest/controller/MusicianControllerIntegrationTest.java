package com.qa.springust.rest.controller;

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
import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.persistence.repository.MusicianRepository;
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
@AutoConfigureMockMvc
class MusicianControllerIntegrationTest {

    // autowiring objects for mocking different aspects of the application
    // here, a mock repo (and relevant mappers) are autowired
    // they'll 'just work', so we don't need to worry about them
    // all we're testing is how our controller integrates with the rest of the API

    private static final MediaType JSON_FORMAT = MediaType.APPLICATION_JSON;

    // mockito's request-making backend - acting as our controller
    // you only need this in integration testing - no mocked service required!
    // this acts as postman would, across your whole application
    @Autowired
    private MockMvc mvc;

    // i'm reusing my normal repo to ping different things to for testing purposes
    // this is only used for my <expected> objects, not <actual> ones!
    @Autowired
    private MusicianRepository repo;

    // this specifically maps objects to JSON format for us
    // slightly different from ModelMapper because this is bundled with mockito
    @Autowired
    private ObjectMapper jsonifier;

    // i've set the root string for our URI to avoid magic strings
    private final String URI = "/musician";

    // set up the variables we'll need for testing
    private Long id;
    private List<MusicianDTO> musicianDTOList;
    private Musician testMusician;
    private Musician testMusicianWithId;
    private MusicianDTO musicianDTOWithId;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.musicianDTOList = new ArrayList<>();
        this.testMusician = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
                MUSICIAN.GUITARIST.getType());
        this.testMusicianWithId = this.repo.saveAndFlush(this.testMusician);
        this.id = this.testMusicianWithId.getId();
        this.musicianDTOWithId = new MusicianDTO(this.id, this.testMusician.getName(), this.testMusician.getStrings(),
                this.testMusician.getType());

    }

    @Test
    void testCreate() throws Exception {
        this.mvc.perform(post(URI + "/create").accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(this.testMusician))).andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.musicianDTOWithId)));
    }

    @Test
    void testReadOne() throws Exception {
        this.mvc.perform(get(URI + "/read/" + this.id).accept(JSON_FORMAT).contentType(JSON_FORMAT))
                .andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.musicianDTOWithId)));
    }

    @Test
    void testReadAll() throws Exception {
        this.musicianDTOList.add(this.musicianDTOWithId);

        this.mvc.perform(get(URI + "/read").accept(JSON_FORMAT).contentType(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.musicianDTOList)));
    }

    @Test
    void testUpdate() throws Exception {
        this.mvc.perform(put(URI + "/update/" + this.id).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(this.musicianDTOWithId))).andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.musicianDTOWithId)));

    }

    @Test
    void testDelete() throws Exception {
        this.mvc.perform(delete(URI + "/delete/" + this.id).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(this.testMusician))).andExpect(status().isNoContent());
    }

}