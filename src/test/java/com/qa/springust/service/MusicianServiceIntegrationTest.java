package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.persistence.repository.MusicianRepository;
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
class MusicianServiceIntegrationTest {

    @Autowired
    private MusicianService service;

    @Autowired
    private MusicianRepository repo;

    @Autowired
    private ModelMapper mapper;

    private MusicianDTO mapToDTO(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private Musician mapToPOJO(MusicianDTO musicianDTO) {
        return this.mapper.map(musicianDTO, Musician.class);
    }

    private final Musician TEST_GUITARIST = new Musician(1L, "John Darnielle", 6, "guitarist");
    private final Musician TEST_SAXOPHONIST = new Musician(2L, "Matt Douglas", 0, "saxophonist");
    private final Musician TEST_BASSIST = new Musician(3L, "Peter Hughes", 4, "bassist");
    private final Musician TEST_DRUMMER = new Musician(4L, "Jon Wurster", 0, "drummer");

    private final List<Musician> MUSICIANS = List.of(TEST_GUITARIST, TEST_SAXOPHONIST, TEST_BASSIST, TEST_DRUMMER);

    @BeforeEach
    void setup() {
        this.repo.saveAll(MUSICIANS);
    }

    @Test
    void createTest() throws Exception {
        assertThat(this.service.create(TEST_GUITARIST)).isEqualTo(this.mapToDTO(TEST_GUITARIST));
    }

    @Test
    void readOneTest() throws Exception {
        assertThat(this.service.read(TEST_GUITARIST.getId())).isEqualTo(this.mapToDTO(TEST_GUITARIST));
    }

    @Test
    void readAllTest() throws Exception {
        assertThat(this.service.read().stream().map(this::mapToPOJO)).isEqualTo(MUSICIANS);

    }

    @Test
    void updateTest() throws Exception {
        assertThat(this.service.update(this.mapToDTO(TEST_GUITARIST), TEST_GUITARIST.getId()))
                .isEqualTo(this.mapToDTO(TEST_GUITARIST));
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(TEST_GUITARIST.getId())).isTrue();
    }

    @Test
    void findByNameTest() throws Exception {
        assertThat(this.service.findByName(TEST_GUITARIST.getName())).isEqualTo(MUSICIANS.stream().map(this::mapToDTO)
                .filter(e -> e.getName().equals(TEST_GUITARIST.getName())).collect(Collectors.toList()));
    }

    @Test
    void findByStringsTest() throws Exception {
        assertThat(this.service.findByStrings(TEST_GUITARIST.getStrings()))
                .isEqualTo(MUSICIANS.stream().map(this::mapToDTO)
                        .filter(e -> e.getName().equals(TEST_GUITARIST.getName())).collect(Collectors.toList()));
    }

    @Test
    void findByTypeTest() throws Exception {
        assertThat(this.service.findByType(TEST_GUITARIST.getType())).isEqualTo(MUSICIANS.stream().map(this::mapToDTO)
                .filter(e -> e.getName().equals(TEST_GUITARIST.getName())).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() throws Exception {
        assertThat(this.service.orderByName().stream().map(e -> e.getName())).isEqualTo(MUSICIANS.stream()
                .sorted(Comparator.comparing(Musician::getName)).map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void orderByStringsTest() throws Exception {
        assertThat(this.service.orderByStrings().stream().map(e -> e.getStrings()))
                .isEqualTo(MUSICIANS.stream().sorted(Comparator.comparing(Musician::getStrings))
                        .map(e -> e.getStrings()).collect(Collectors.toList()));
    }

    @Test
    void orderByTypeTest() throws Exception {
        assertThat(this.service.orderByType().stream().map(e -> e.getType())).isEqualTo(MUSICIANS.stream()
                .sorted(Comparator.comparing(Musician::getType)).map(e -> e.getType()).collect(Collectors.toList()));
    }

}
