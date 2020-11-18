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

import com.qa.springust.global.BAND;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;
import com.qa.springust.rest.dto.BandDTO;

@SpringBootTest
class BandServiceIntegrationTest {

    @Autowired
    private BandService service;

    @Autowired
    private BandRepository repo;

    @Autowired
    private ModelMapper mapper;

    private BandDTO map(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Band TEST_BAND = new Band(TEST_ID, BAND.TMG.getName());

    @Test
    void createTest() throws Exception {
        BandDTO expected = this.map(TEST_BAND);
        assertThat(this.service.create(TEST_BAND)).isEqualTo(expected);
    }

    @Test
    void readOneTest() throws Exception {
        this.repo.save(TEST_BAND);
        BandDTO expected = this.map(TEST_BAND);

        assertThat(this.service.read(TEST_ID)).isEqualTo(expected);
    }

    @Test
    void readAllTest() throws Exception {
        List<BandDTO> bands = new ArrayList<>();
        BandDTO expected = this.map(TEST_BAND);
        bands.add(expected);
        this.repo.save(TEST_BAND);

        assertThat(this.service.read()).isEqualTo(Stream.of(bands).collect(Collectors.toList()).get(0));
    }

    @Test
    void updateTest() throws Exception {
        BandDTO expected = this.map(TEST_BAND);
        assertThat(this.service.update(expected, TEST_ID)).isEqualTo(expected);
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(TEST_ID)).isTrue();
    }

}
