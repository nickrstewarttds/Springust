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

    private BandDTO mapToDTO(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    private Band mapToPOJO(BandDTO bandDTO) {
        return this.mapper.map(bandDTO, Band.class);
    }

    private final Long TEST_ID = 1L;

    private final Band TEST_BAND1 = new Band(TEST_ID, BAND.TMG.getName());
    private final Band TEST_BAND2 = new Band(TEST_ID + 1, BAND.TEG.getName());
    private final Band TEST_BAND3 = new Band(TEST_ID + 2, BAND.TEL.getName());

    @Test
    void createTest() throws Exception {
        BandDTO expected = this.mapToDTO(TEST_BAND1);
        assertThat(this.service.create(TEST_BAND1)).isEqualTo(expected);
    }

    @Test
    void readOneTest() throws Exception {
        this.repo.save(TEST_BAND1);
        BandDTO expected = this.mapToDTO(TEST_BAND1);
        assertThat(this.service.read(TEST_ID)).isEqualTo(expected);
    }

    @Test
    void readAllTest() throws Exception {
        this.repo.save(TEST_BAND1);
        this.repo.save(TEST_BAND2);
        this.repo.save(TEST_BAND3);

        List<BandDTO> bands = new ArrayList<>();
        bands.add(this.mapToDTO(TEST_BAND1));
        bands.add(this.mapToDTO(TEST_BAND2));
        bands.add(this.mapToDTO(TEST_BAND3));

        assertThat(this.service.read().stream().map(this::mapToPOJO))
                .isEqualTo(bands.stream().map(this::mapToPOJO).collect(Collectors.toList()));
    }

    @Test
    void updateTest() throws Exception {
        BandDTO expected = this.mapToDTO(TEST_BAND1);
        assertThat(this.service.update(expected, TEST_ID)).isEqualTo(expected);
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(TEST_ID)).isTrue();
    }

    @Test
    void findByNameTest() throws Exception {
        this.repo.save(TEST_BAND1);
        BandDTO expected = this.mapToDTO(TEST_BAND1);
        List<BandDTO> bands = new ArrayList<>();
        bands.add(expected);
        assertThat(this.service.findByName(TEST_BAND1.getName())).isEqualTo(bands);
    }

    @Test
    void orderByNameTest() throws Exception {
        this.repo.save(TEST_BAND1);
        this.repo.save(TEST_BAND2);
        this.repo.save(TEST_BAND3);

        List<Band> bands = new ArrayList<>();
        bands.add(TEST_BAND1);
        bands.add(TEST_BAND2);
        bands.add(TEST_BAND3);

        assertThat(this.service.orderByName().stream().map(e -> e.getName())).isEqualTo(bands.stream()
                .sorted(Comparator.comparing(Band::getName)).map(e -> e.getName()).collect(Collectors.toList()));
    }

}
