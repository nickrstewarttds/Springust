package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private MusicianDTO map(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Musician TEST_MUSICIAN = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());

    @Test
    void createTest() throws Exception {
        MusicianDTO expected = this.map(TEST_MUSICIAN);
        assertThat(this.service.create(TEST_MUSICIAN)).isEqualTo(expected);
    }

    @Test
    void readOneTest() throws Exception {
        this.repo.save(TEST_MUSICIAN);
        MusicianDTO expected = this.map(TEST_MUSICIAN);
        assertThat(this.service.read(TEST_ID)).isEqualTo(expected);
    }

    @Test
    void readAllTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        MusicianDTO expected = this.map(TEST_MUSICIAN);
        musicians.add(expected);
        this.repo.save(TEST_MUSICIAN);
        assertThat(this.service.read()).isEqualTo(Stream.of(musicians).collect(Collectors.toList()).get(0));
    }

    @Test
    void updateTest() throws Exception {
        MusicianDTO expected = this.map(TEST_MUSICIAN);
        assertThat(this.service.update(expected, TEST_ID)).isEqualTo(expected);
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(TEST_ID)).isTrue();
    }

}
