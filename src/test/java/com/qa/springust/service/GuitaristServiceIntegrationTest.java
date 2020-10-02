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
    private GuitaristDTO testGuitaristDTO;

    private Long id;
    private final String name = "John Darnielle";
    private final Integer strings = 6;
    private final String type = "Ibanez Talman";

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testGuitarist = new Guitarist(name, strings, type);
        this.testGuitaristWithId = this.repo.save(this.testGuitarist);
        this.testGuitaristDTO = this.mapToDTO(testGuitaristWithId);
        this.id = this.testGuitaristWithId.getId();
    }

    @Test
    void testCreate() {
        assertThat(this.testGuitaristDTO)
            .isEqualTo(this.service.create(testGuitarist));
    }

    @Test
    void testRead() {
        assertThat(this.testGuitaristDTO)
                .isEqualTo(this.service.read(this.id));
    }

    @Test
    void testReadAll() {
        // check this one out with a breakpoint and running it in debug mode
        // so you can see the stream happening
        assertThat(this.service.read())
                .isEqualTo(Stream.of(this.testGuitaristDTO)
                        .collect(Collectors.toList()));
    }

    @Test
    void testUpdate() {
        GuitaristDTO newGuitarist = new GuitaristDTO(null, "Peter Peter Hughes", 4, "Fender American");
        GuitaristDTO updatedGuitarist =
                new GuitaristDTO(this.id, newGuitarist.getName(), newGuitarist.getStrings(), newGuitarist.getType());

        assertThat(updatedGuitarist)
            .isEqualTo(this.service.update(newGuitarist, this.id));
    }

    @Test
    void testDelete() {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
