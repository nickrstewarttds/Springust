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
import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.persistence.domain.Guitarist;
import com.qa.springust.persistence.repository.GuitaristRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class GuitaristControllerIntegrationTest {

    // autowiring objects for mocking different aspects of the application
    // here, a mock repo (and relevant mappers) are autowired
    // they'll 'just work', so we don't need to worry about them
    // all we're testing is how our controller integrates with the rest of the API

    private static final MediaType jsonFormat = MediaType.APPLICATION_JSON;

    // mockito's request-making backend - acting as our controller
    // you only need this in integration testing - no mocked service required!
    // this acts as postman would, across your whole application
    @Autowired
    private MockMvc mvc;

    // i'm reusing my normal repo to ping different things to for testing purposes
    // this is only used for my <expected> objects, not <actual> ones!
    @Autowired
    private GuitaristRepository repo;

    // this specifically maps objects to JSON format for us
    // slightly different from ModelMapper because this is bundled with mockito
    @Autowired
    private ObjectMapper jsonifier;

    private Long id;
    private Guitarist testGuitarist;
    private Guitarist testGuitaristWithId;
    private GuitaristDTO guitaristDTO;

    @BeforeEach
    void init() {
        this.repo.deleteAll();

        this.testGuitarist = new Guitarist("John Darnielle", 6, "Guitar");
        this.testGuitaristWithId = this.repo.save(this.testGuitarist);
        this.id = this.testGuitaristWithId.getId();
        this.guitaristDTO = new GuitaristDTO(this.id, testGuitarist.getName(), testGuitarist.getStrings(), testGuitarist.getType());
    }

    @Test
    void testCreate() throws Exception {
        this.mvc.perform(post("/guitarist/create")
                .accept(jsonFormat)
                .contentType(jsonFormat)
                .content(this.jsonifier.writeValueAsString(this.testGuitarist)))
            .andExpect(status().isCreated())
            .andExpect(content().json(this.jsonifier.writeValueAsString(this.guitaristDTO)));
    }

    @Test
    void testReadOne() throws Exception {
        this.mvc.perform(get("/guitarist/read/" + this.id)
                .accept(jsonFormat)
                .contentType(jsonFormat))
            .andExpect(status().isOk())
            .andExpect(content().json(this.jsonifier.writeValueAsString(this.guitaristDTO)));
    }

    @Test
    void testReadAll() throws Exception {
        final List<GuitaristDTO> GUITARISTS = new ArrayList<>();
        GUITARISTS.add(this.guitaristDTO);
        
        this.mvc.perform(get("/guitarist/read")
                .accept(jsonFormat)
                .contentType(jsonFormat))
            .andExpect(status().isOk())
            .andExpect(content().json(this.jsonifier.writeValueAsString(GUITARISTS)));
    }

    @Test
    void testUpdate() throws Exception {
        final GuitaristDTO NEW_GUITARIST_DTO = new GuitaristDTO(this.id, "PPH", 4, "Bass");
        
        this.mvc.perform(put("/guitarist/update/" + this.id)
                .accept(jsonFormat)
                .contentType(jsonFormat)
                .content(this.jsonifier.writeValueAsString(NEW_GUITARIST_DTO)))
            .andExpect(status().isAccepted())
            .andExpect(content().json(this.jsonifier.writeValueAsString(NEW_GUITARIST_DTO)));
        
    }

    @Test
    void testDelete() throws Exception {
        this.mvc.perform(delete("/guitarist/delete/" + this.id)
                .accept(jsonFormat)
                .contentType(jsonFormat)
                .content(this.jsonifier.writeValueAsString(this.testGuitarist)))
            .andExpect(status().isNoContent());
    }

}