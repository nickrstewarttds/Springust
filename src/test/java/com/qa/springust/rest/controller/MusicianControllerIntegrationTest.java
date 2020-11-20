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
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(profiles = "test")
class MusicianControllerIntegrationTest {

    private static final MediaType JSON_FORMAT = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper jsonifier;

    @Autowired
    private ModelMapper mapper;

    private MusicianDTO map(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private final String URI = "/musician";

    private final Long TEST_ID = 1L;

    private final Musician TEST_GUITARIST = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());
    private final Musician TEST_SAXOPHONIST = new Musician(TEST_ID + 1, MUSICIAN.SAXOPHONIST.getName(),
            MUSICIAN.SAXOPHONIST.getStrings(), MUSICIAN.SAXOPHONIST.getType());
    private final Musician TEST_BASSIST = new Musician(TEST_ID + 2, MUSICIAN.BASSIST.getName(),
            MUSICIAN.BASSIST.getStrings(), MUSICIAN.BASSIST.getType());
    private final Musician TEST_DRUMMER = new Musician(TEST_ID + 3, MUSICIAN.DRUMMER.getName(),
            MUSICIAN.DRUMMER.getStrings(), MUSICIAN.DRUMMER.getType());

    private final Band TEST_BAND = new Band(TEST_ID, BAND.TMG.getName());

    @Test
    void testCreate() throws Exception {
        TEST_GUITARIST.setId(TEST_ID + 1);
        TEST_GUITARIST.setBand(TEST_BAND);
        MusicianDTO expected = this.map(TEST_GUITARIST);

        this.mvc.perform(post(URI + "/create").accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_GUITARIST))).andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testReadOne() throws Exception {
        MusicianDTO expected = this.map(TEST_GUITARIST);

        this.mvc.perform(get(URI + "/read/" + TEST_ID).accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testReadAll() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_GUITARIST));
        musicians.add(this.map(TEST_SAXOPHONIST));
        musicians.add(this.map(TEST_BASSIST));
        musicians.add(this.map(TEST_DRUMMER));

        this.mvc.perform(get(URI + "/read").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(musicians)));
    }

    @Test
    void testUpdate() throws Exception {
        MusicianDTO expected = this.map(TEST_GUITARIST);

        this.mvc.perform(put(URI + "/update/" + TEST_ID).accept(JSON_FORMAT).contentType(JSON_FORMAT)
                .content(this.jsonifier.writeValueAsString(TEST_GUITARIST))).andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(expected)));
    }

    @Test
    void testDelete() throws Exception {
        this.mvc.perform(delete(URI + "/delete/" + TEST_ID)).andExpect(status().isNoContent());
    }

    @Test
    void findByNameTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_GUITARIST));
        musicians.add(this.map(TEST_SAXOPHONIST));
        musicians.add(this.map(TEST_BASSIST));
        musicians.add(this.map(TEST_DRUMMER));

        this.mvc.perform(get(URI + "/readBy/name/" + TEST_GUITARIST.getName()).accept(JSON_FORMAT))
                .andExpect(content().json(this.jsonifier.writeValueAsString(musicians.stream()
                        .filter(e -> e.getName().equals(TEST_GUITARIST.getName())).collect(Collectors.toList()))));
    }

    @Test
    void findByStringsTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_GUITARIST));
        musicians.add(this.map(TEST_SAXOPHONIST));
        musicians.add(this.map(TEST_BASSIST));
        musicians.add(this.map(TEST_DRUMMER));

        this.mvc.perform(get(URI + "/readBy/strings/" + TEST_GUITARIST.getStrings()).accept(JSON_FORMAT))
                .andExpect(content().json(this.jsonifier.writeValueAsString(
                        musicians.stream().filter(e -> e.getStrings().equals(TEST_GUITARIST.getStrings()))
                                .collect(Collectors.toList()))));
    }

    @Test
    void findByTypeTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_GUITARIST));
        musicians.add(this.map(TEST_SAXOPHONIST));
        musicians.add(this.map(TEST_BASSIST));
        musicians.add(this.map(TEST_DRUMMER));

        this.mvc.perform(get(URI + "/readBy/type/" + TEST_GUITARIST.getType()).accept(JSON_FORMAT))
                .andExpect(content().json(this.jsonifier.writeValueAsString(musicians.stream()
                        .filter(e -> e.getType().equals(TEST_GUITARIST.getType())).collect(Collectors.toList()))));
    }

    @Test
    void orderByNameTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_GUITARIST));
        musicians.add(this.map(TEST_SAXOPHONIST));
        musicians.add(this.map(TEST_BASSIST));
        musicians.add(this.map(TEST_DRUMMER));

        this.mvc.perform(get(URI + "/read/names").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(musicians)));
    }

    @Test
    void orderByStringsTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_DRUMMER));
        musicians.add(this.map(TEST_SAXOPHONIST));
        musicians.add(this.map(TEST_BASSIST));
        musicians.add(this.map(TEST_GUITARIST));

        this.mvc.perform(get(URI + "/read/strings").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(musicians)));
    }

    @Test
    void orderByTypeTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.map(TEST_BASSIST));
        musicians.add(this.map(TEST_DRUMMER));
        musicians.add(this.map(TEST_GUITARIST));
        musicians.add(this.map(TEST_SAXOPHONIST));

        this.mvc.perform(get(URI + "/read/types").accept(JSON_FORMAT)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(musicians)));
    }

}