package com.qa.springust.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private final Band TEST_BAND1 = new Band(TEST_ID, BAND.TMG.getName());
    private final Band TEST_BAND2 = new Band(TEST_ID + 1, BAND.TEG.getName());
    private final Band TEST_BAND3 = new Band(TEST_ID + 2, BAND.TEL.getName());

    private final Musician TEST_GUITARIST = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());
    private final Musician TEST_SAXOPHONIST = new Musician(TEST_ID + 1, MUSICIAN.SAXOPHONIST.getName(),
            MUSICIAN.SAXOPHONIST.getStrings(), MUSICIAN.SAXOPHONIST.getType());
    private final Musician TEST_BASSIST = new Musician(TEST_ID + 2, MUSICIAN.BASSIST.getName(),
            MUSICIAN.BASSIST.getStrings(), MUSICIAN.BASSIST.getType());
    private final Musician TEST_DRUMMER = new Musician(TEST_ID + 3, MUSICIAN.DRUMMER.getName(),
            MUSICIAN.DRUMMER.getStrings(), MUSICIAN.DRUMMER.getType());

    @Test
    void createTest() throws Exception {
        TEST_BAND1.setId(TEST_ID + 1);
        BandDTO expected = this.map(TEST_BAND1);

        this.mvc.perform(post(URI + "/create").accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_BAND1))).andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void readOneTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);

        TEST_BAND1.setMusicians(musicians);
        BandDTO expected = this.map(TEST_BAND1);

        this.mvc.perform(get(URI + "/read/" + TEST_ID).accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void readAllTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);

        TEST_BAND1.setMusicians(musicians);

        List<BandDTO> bands = new ArrayList<>();
        bands.add(this.map(TEST_BAND1));
        bands.add(this.map(TEST_BAND2));
        bands.add(this.map(TEST_BAND3));

        this.mvc.perform(get(URI + "/read").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(bands)));
    }

    @Test
    void updateTest() throws Exception {
        BandDTO expected = this.map(TEST_BAND1);

        this.mvc.perform(put(URI + "/update/" + TEST_ID).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_BAND1))).andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testDelete() throws Exception {
        this.mvc.perform(delete(URI + "/delete/" + TEST_ID)).andExpect(status().isNoContent());
    }

    @Test
    void findByNameTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);

        TEST_BAND1.setMusicians(musicians);

        List<BandDTO> bands = new ArrayList<>();
        bands.add(this.map(TEST_BAND1));
        bands.add(this.map(TEST_BAND2));
        bands.add(this.map(TEST_BAND3));

        this.mvc.perform(get(URI + "/readBy/" + TEST_BAND1.getName()).accept(JSON_FORMAT))
                .andExpect(content().json(this.jsonifier.writeValueAsString(bands.stream()
                        .filter(e -> e.getName().equals(TEST_BAND1.getName())).collect(Collectors.toList()))));
    }

    @Test
    void orderByNameTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);

        TEST_BAND1.setMusicians(musicians);

        List<BandDTO> bands = new ArrayList<>();
        bands.add(this.map(TEST_BAND2));
        bands.add(this.map(TEST_BAND3));
        bands.add(this.map(TEST_BAND1));

        this.mvc.perform(get(URI + "/read/names").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(bands)));
    }

}
