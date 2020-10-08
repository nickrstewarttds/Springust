package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.persistence.domain.Guitarist;
import com.qa.springust.persistence.repository.GuitaristRepository;

@SpringBootTest
class GuitaristServiceIntegrationTest {

    // because we're testing the service layer, we can't use a MockMvc
    // because MockMvc only models a controller (in mockito format)

    @Autowired
    private GuitaristService service;

    @Autowired
    private GuitaristRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    private GuitaristDTO mapToDTO(Guitarist guitarist) {
        return this.modelMapper.map(guitarist, GuitaristDTO.class);
    }

    // there's no objectMapper this time
    // because we don't need to convert any returned objects to JSON
    // that's a controller job, and we're not testing the controller! :D

    private Guitarist testGuitarist;
    private Guitarist testGuitaristWithId;
    private GuitaristDTO guitaristDTO;

    private Long id;

    private final String name = "John Darnielle";
    private final Integer strings = 6;
    private final String type = "Guitar";

    private final String updatedName = "PPH";
    private final Integer updatedStrings = 4;
    private final String updatedType = "Bass";

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testGuitarist = new Guitarist(this.name, this.strings, this.type);
        this.testGuitaristWithId = this.repo.save(this.testGuitarist);
        this.guitaristDTO = this.mapToDTO(this.testGuitaristWithId);
        this.id = this.testGuitaristWithId.getId();
    }

    @Test
    void createTest() {
        assertThat(this.guitaristDTO).isEqualTo(this.service.create(this.testGuitarist));
    }

    @Test
    void readOneTest() {
        assertThat(this.guitaristDTO).isEqualTo(this.service.read(this.id));
    }

    @Test
    void readAllTest() {
        // check this one out with a breakpoint and running it in debug mode
        // so you can see the stream happening
        assertThat(this.service.read()).isEqualTo(Stream.of(this.guitaristDTO).collect(Collectors.toList()));
    }

    @Test
    void updateTest() {
        GuitaristDTO oldDTO = new GuitaristDTO(null, this.updatedName, this.updatedStrings, this.updatedType);
        GuitaristDTO newDTO = new GuitaristDTO(this.id, oldDTO.getName(), oldDTO.getStrings(), oldDTO.getType());

        assertThat(newDTO).isEqualTo(this.service.update(oldDTO, this.id));
    }

    @Test
    void deleteTest() {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
