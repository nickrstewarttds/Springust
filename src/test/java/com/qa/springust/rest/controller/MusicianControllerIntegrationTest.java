package com.qa.springust.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(profiles = "test")
class MusicianControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper jsonifier;

    @Autowired
    private ModelMapper mapper;

    private MusicianDTO mapToDTO(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private final String URI = "/musician";

    private final Band TEST_BAND = new Band(1L, "The Mountain Goats");

    private final Musician TEST_GUITARIST = new Musician(1L, "John Darnielle", 6, "guitarist", TEST_BAND);
    private final Musician TEST_SAXOPHONIST = new Musician(2L, "Matt Douglas", 0, "saxophonist", TEST_BAND);
    private final Musician TEST_BASSIST = new Musician(3L, "Peter Hughes", 4, "bassist", TEST_BAND);
    private final Musician TEST_DRUMMER = new Musician(4L, "Jon Wurster", 0, "drummer", TEST_BAND);

    private final List<Musician> MUSICIANS = List.of(TEST_GUITARIST, TEST_SAXOPHONIST, TEST_BASSIST, TEST_DRUMMER);

    @Test
    void createTest() throws Exception {
        this.mvc.perform(post(URI + "/create").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(this.jsonifier.writeValueAsString(TEST_GUITARIST)))
                .andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.mapToDTO(TEST_GUITARIST))));
    }

    @Test
    void readOneTest() throws Exception {
        this.mvc.perform(get(URI + "/read/" + TEST_GUITARIST.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.mapToDTO(TEST_GUITARIST))));
    }

    @Test
    void readAllTest() throws Exception {
        this.mvc.perform(get(URI + "/read").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier
                        .writeValueAsString(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()))));
    }

    @Test
    void updateTest() throws Exception {
        this.mvc.perform(put(URI + "/update/" + TEST_GUITARIST.getId()).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(this.jsonifier.writeValueAsString(TEST_GUITARIST)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.mapToDTO(TEST_GUITARIST))));
    }

    @Test
    void deleteTest() throws Exception {
        this.mvc.perform(delete(URI + "/delete/" + TEST_GUITARIST.getId())).andExpect(status().isNoContent());
    }

    @Test
    void findByNameTest() throws Exception {
        this.mvc.perform(get(URI + "/readBy/name/" + TEST_GUITARIST.getName()).accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(this.jsonifier.writeValueAsString(MUSICIANS.stream().map(this::mapToDTO)
                        .filter(e -> e.getName().equals(TEST_GUITARIST.getName())).collect(Collectors.toList()))));
    }

    @Test
    void findByStringsTest() throws Exception {
        this.mvc.perform(get(URI + "/readBy/strings/" + TEST_GUITARIST.getStrings()).accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(this.jsonifier.writeValueAsString(MUSICIANS.stream().map(this::mapToDTO)
                        .filter(e -> e.getStrings().equals(TEST_GUITARIST.getStrings()))
                        .collect(Collectors.toList()))));
    }

    @Test
    void findByTypeTest() throws Exception {
        this.mvc.perform(get(URI + "/readBy/type/" + TEST_GUITARIST.getType()).accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(this.jsonifier.writeValueAsString(MUSICIANS.stream().map(this::mapToDTO)
                        .filter(e -> e.getType().equals(TEST_GUITARIST.getType())).collect(Collectors.toList()))));
    }

    @Test
    void orderByNameTest() throws Exception {
        this.mvc.perform(get(URI + "/read/names").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier
                        .writeValueAsString(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()))));
    }

    @Test
    void orderByStringsTest() throws Exception {
        this.mvc.perform(get(URI + "/read/strings").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier
                        .writeValueAsString(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()))));
    }

    @Test
    void orderByTypeTest() throws Exception {
        this.mvc.perform(get(URI + "/read/types").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier
                        .writeValueAsString(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()))));
    }

}