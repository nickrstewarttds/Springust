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
import com.qa.springust.global.BAND;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;
import com.qa.springust.rest.dto.BandDTO;
import com.qa.springust.rest.dto.MusicianDTO;
import com.qa.springust.service.BandService;

@SpringBootTest
@AutoConfigureMockMvc
class BandControllerIntegrationTest {

    private static final MediaType JSON_FORMAT = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BandService serviceMock;

    @Autowired
    private BandRepository repo;

    @Autowired
    private ObjectMapper jsonifier;

    private final String URI = "/band";
    private final String DUMMY = "foo";

    private Long id;
    private List<BandDTO> bandDTOList;
    private List<MusicianDTO> musicianDTOList;
    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTOWithId;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.bandDTOList = new ArrayList<>();
        this.musicianDTOList = new ArrayList<>();
        this.testBand = new Band(BAND.TMG.getName());
        this.testBandWithId = this.repo.saveAndFlush(this.testBand);
        this.id = this.testBandWithId.getId();
        this.bandDTOWithId = new BandDTO(this.id, this.testBand.getName(), this.musicianDTOList);
    }

    @Test
    void testCreate() throws Exception {
        this.mvc.perform(post(this.URI + "/create").accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(this.testBand))).andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.bandDTOWithId)));
    }

    @Test
    void testReadOne() throws Exception {
        this.mvc.perform(get(this.URI + "/read/" + this.id).accept(JSON_FORMAT).contentType(JSON_FORMAT))
                .andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.bandDTOWithId)));
    }

    @Test
    void testReadAll() throws Exception {
        this.bandDTOList.add(this.bandDTOWithId);

        this.mvc.perform(get(this.URI + "/read").accept(JSON_FORMAT).contentType(JSON_FORMAT))
                .andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.bandDTOList)));
    }

    @Test
    void testUpdate() throws Exception {
        this.mvc.perform(put(this.URI + "/update/" + this.id).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(this.bandDTOWithId))).andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.bandDTOWithId)));
    }

    @Test
    void testDelete() throws Exception {
        this.mvc.perform(delete(this.URI + "/delete/" + this.id).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(this.testBand))).andExpect(status().isNoContent());
    }

}
