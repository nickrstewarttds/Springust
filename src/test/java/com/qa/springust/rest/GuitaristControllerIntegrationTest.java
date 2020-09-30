package com.qa.springust.rest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
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

    @Autowired
    private MockMvc mock;

    @Autowired
    private GuitaristRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Guitarist testGuitarist;
    private Guitarist testGuitaristWithId;
    private GuitaristDTO guitaristDTO;

    private Long id;
    private String testName;
    private Integer testStrings;
    private String testType;

    private GuitaristDTO mapToDTO(Guitarist guitarist) {
        return this.modelMapper.map(guitarist, GuitaristDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();

        this.testGuitarist = new Guitarist("John Darnielle", 6, "Ibanez Talman");
        this.testGuitaristWithId = this.repo.save(this.testGuitarist);
        this.guitaristDTO = this.mapToDTO(testGuitaristWithId);

        this.id = this.testGuitaristWithId.getId();
        this.testName = this.testGuitaristWithId.getName();
        this.testStrings = this.testGuitaristWithId.getStrings();
        this.testType = this.testGuitaristWithId.getType();
    }

    @Test
    void testCreate() throws Exception {
        this.mock
                .perform(request(HttpMethod.POST, "/guitarist/create").contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(testGuitarist))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(this.objectMapper.writeValueAsString(guitaristDTO)));
    }

    @Test
    void testRead() throws Exception {
        this.mock.perform(request(HttpMethod.GET, "/guitarist/read/" + this.id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.guitaristDTO)));
    }

    @Test
    void testReadAll() throws Exception {
        List<GuitaristDTO> guitaristList = new ArrayList<>();
        guitaristList.add(this.guitaristDTO);

        String content = this.mock
                .perform(request(HttpMethod.GET, "/guitarist/read").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(guitaristList), content);
    }

    @Test
    void testUpdate() throws Exception {
        GuitaristDTO newGuitarist = new GuitaristDTO(null, "Peter Peter Hughes", 4, "Fender American");
        Guitarist updatedGuitarist = new Guitarist(newGuitarist.getName(), newGuitarist.getStrings(),
                newGuitarist.getType());
        updatedGuitarist.setId(this.id);

        String result = this.mock
                .perform(request(HttpMethod.PUT, "/guitarist/update/" + this.id).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newGuitarist)))
                .andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(this.mapToDTO(updatedGuitarist)), result);
    }

    @Test
    void testDelete() throws Exception {
        this.mock.perform(request(HttpMethod.DELETE, "/guitarist/delete/" + this.id)).andExpect(status().isNoContent());
    }

    @Test
    void testfindByNameJPQL() throws Exception {
        List<GuitaristDTO> guitaristList = new ArrayList<>();
        guitaristList.add(this.guitaristDTO);

        String content = this.mock
                .perform(request(HttpMethod.GET, "/guitarist/searchName/" + this.testName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(guitaristList), content);
    }

    @Test
    void testfindByStringsJPQL() throws Exception {
        List<GuitaristDTO> guitaristList = new ArrayList<>();
        guitaristList.add(this.guitaristDTO);

        String content = this.mock
                .perform(request(HttpMethod.GET, "/guitarist/searchStrings/" + this.testStrings)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(guitaristList), content);
    }

    @Test
    void testfindByTypeJPQL() throws Exception {
        List<GuitaristDTO> guitaristList = new ArrayList<>();
        guitaristList.add(this.guitaristDTO);

        String content = this.mock
                .perform(request(HttpMethod.GET, "/guitarist/searchType/" + this.testType)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(guitaristList), content);
    }

}
