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
import com.qa.springust.rest.dto.BandDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(profiles = "test")
class BandControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper mapper;

    private BandDTO mapToDTO(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    @Autowired
    private ObjectMapper jsonifier;

    private final String URI = "/band";

    private final Band TEST_BAND1 = new Band(1L, "The Mountain Goats");
    private final Band TEST_BAND2 = new Band(2L, "The Extra Glenns");
    private final Band TEST_BAND3 = new Band(3L, "The Congress");

    private final List<Band> BANDS = List.of(TEST_BAND1, TEST_BAND2, TEST_BAND3);

    private final Musician TEST_GUITARIST = new Musician(1L, "John Darnielle", 6, "guitarist");
    private final Musician TEST_SAXOPHONIST = new Musician(2L, "Matt Douglas", 0, "saxophonist");
    private final Musician TEST_BASSIST = new Musician(3L, "Peter Hughes", 4, "bassist");
    private final Musician TEST_DRUMMER = new Musician(4L, "Jon Wurster", 0, "drummer");

    private final List<Musician> MUSICIANS = List.of(TEST_GUITARIST, TEST_SAXOPHONIST, TEST_BASSIST, TEST_DRUMMER);

    @Test
    void createTest() throws Exception {
        this.mvc.perform(post(URI + "/create").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(this.jsonifier.writeValueAsString(TEST_BAND1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.mapToDTO(TEST_BAND1))));
    }

    @Test
    void readOneTest() throws Exception {
        TEST_BAND1.setMusicians(MUSICIANS);
        this.mvc.perform(get(URI + "/read/" + TEST_BAND1.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.mapToDTO(TEST_BAND1))));
    }

    @Test
    void readAllTest() throws Exception {
        TEST_BAND1.setMusicians(MUSICIANS);
        this.mvc.perform(get(URI + "/read").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier
                        .writeValueAsString(BANDS.stream().map(this::mapToDTO).collect(Collectors.toList()))));
    }

    @Test
    void updateTest() throws Exception {
        this.mvc.perform(put(URI + "/update/" + TEST_BAND1.getId()).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(this.jsonifier.writeValueAsString(TEST_BAND1)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(this.jsonifier.writeValueAsString(this.mapToDTO(TEST_BAND1))));
    }

    @Test
    void deleteTest() throws Exception {
        this.mvc.perform(delete(URI + "/delete/" + TEST_BAND1.getId())).andExpect(status().isNoContent());
    }

    @Test
    void findByNameTest() throws Exception {
        TEST_BAND1.setMusicians(MUSICIANS);
        this.mvc.perform(get(URI + "/readBy/name/" + TEST_BAND1.getName()).accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(this.jsonifier.writeValueAsString(BANDS.stream().map(this::mapToDTO)
                        .filter(e -> e.getName().equals(TEST_BAND1.getName())).collect(Collectors.toList()))));
    }

    @Test
    void orderByNameTest() throws Exception {
        TEST_BAND1.setMusicians(MUSICIANS);
        this.mvc.perform(get(URI + "/read/names").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(this.jsonifier
                        .writeValueAsString(BANDS.stream().map(this::mapToDTO).collect(Collectors.toList()))));
    }

}
