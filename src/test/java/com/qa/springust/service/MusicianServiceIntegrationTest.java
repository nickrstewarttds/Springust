package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.persistence.repository.MusicianRepository;
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
class MusicianServiceIntegrationTest {

    // because we're testing the service layer, we can't use a MockMvc
    // because MockMvc only models a controller (in mockito format)

    @Autowired
    private MusicianService service;

    @Autowired
    private MusicianRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    private MusicianDTO mapToDTO(Musician musician) {
        return this.modelMapper.map(musician, MusicianDTO.class);
    }

    // there's no objectMapper this time
    // because we don't need to convert any returned objects to JSON
    // that's a controller job, and we're not testing the controller! :D

    private Long id;

    private Musician testMusician;
    private Musician testMusicianWithId;
    private MusicianDTO musicianDTO;
    private MusicianDTO musicianDTOWithId;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testMusician = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
                MUSICIAN.GUITARIST.getType());
        this.testMusicianWithId = this.repo.save(this.testMusician);
        this.id = this.testMusicianWithId.getId();
        this.musicianDTOWithId = this.mapToDTO(this.testMusicianWithId);
        this.musicianDTO = new MusicianDTO(null, this.testMusician.getName(), this.testMusician.getStrings(),
                this.testMusician.getType());
    }

    @Test
    void createTest() throws Exception {
        assertThat(this.musicianDTOWithId).isEqualTo(this.service.create(this.testMusician));
    }

    @Test
    void readOneTest() throws Exception {
        assertThat(this.musicianDTOWithId).isEqualTo(this.service.read(this.id));
    }

    @Test
    void readAllTest() throws Exception {
        // check this one out with a breakpoint and running it in debug mode
        // so you can see the stream happening
        assertThat(Stream.of(this.musicianDTOWithId).collect(Collectors.toList())).isEqualTo(this.service.read());
    }

    @Test
    void updateTest() throws Exception {
        assertThat(this.musicianDTOWithId).isEqualTo(this.service.update(this.musicianDTO, this.id));
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
