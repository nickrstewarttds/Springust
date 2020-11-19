package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private final Long TEST_ID = 1L;

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
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);
        assertThat(this.service.create(TEST_GUITARIST)).isEqualTo(expected);
    }

    @Test
    void readOneTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);
        assertThat(this.service.read(TEST_ID)).isEqualTo(expected);
    }

    @Test
    void readAllTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        this.repo.save(TEST_SAXOPHONIST);
        this.repo.save(TEST_BASSIST);
        this.repo.save(TEST_DRUMMER);

        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.mapToDTO(TEST_GUITARIST));
        musicians.add(this.mapToDTO(TEST_SAXOPHONIST));
        musicians.add(this.mapToDTO(TEST_BASSIST));
        musicians.add(this.mapToDTO(TEST_DRUMMER));

        assertThat(this.service.read().stream().map(this::mapToPOJO))
                .isEqualTo(musicians.stream().map(this::mapToPOJO).collect(Collectors.toList()));

    }

    @Test
    void updateTest() throws Exception {
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);
        assertThat(this.service.update(expected, TEST_ID)).isEqualTo(expected);
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(TEST_ID)).isTrue();
    }

    @Test
    void findByNameTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(expected);
        assertThat(this.service.findByName(TEST_GUITARIST.getName())).isEqualTo(musicians);
    }

    @Test
    void findByStringsTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(expected);
        assertThat(this.service.findByStrings(TEST_GUITARIST.getStrings())).isEqualTo(musicians);
    }

    @Test
    void findByTypeTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(expected);
        assertThat(this.service.findByType(TEST_GUITARIST.getType())).isEqualTo(musicians);
    }

    @Test
    void orderByNameTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        this.repo.save(TEST_SAXOPHONIST);
        this.repo.save(TEST_BASSIST);
        this.repo.save(TEST_DRUMMER);

        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);

        assertThat(this.service.orderByName().stream().map(e -> e.getName())).isEqualTo(musicians.stream()
                .sorted(Comparator.comparing(Musician::getName)).map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void orderByStringsTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        this.repo.save(TEST_SAXOPHONIST);
        this.repo.save(TEST_BASSIST);
        this.repo.save(TEST_DRUMMER);

        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);

        assertThat(this.service.orderByStrings().stream().map(e -> e.getStrings()))
                .isEqualTo(musicians.stream().sorted(Comparator.comparing(Musician::getStrings))
                        .map(e -> e.getStrings()).collect(Collectors.toList()));
    }

    @Test
    void orderByTypeTest() throws Exception {
        this.repo.save(TEST_GUITARIST);
        this.repo.save(TEST_SAXOPHONIST);
        this.repo.save(TEST_BASSIST);
        this.repo.save(TEST_DRUMMER);

        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);

        assertThat(this.service.orderByType().stream().map(e -> e.getType())).isEqualTo(musicians.stream()
                .sorted(Comparator.comparing(Musician::getType)).map(e -> e.getType()).collect(Collectors.toList()));
    }

}
