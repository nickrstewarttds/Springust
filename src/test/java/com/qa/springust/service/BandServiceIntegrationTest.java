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

    private final Band TEST_BAND1 = new Band(1L, "The Mountain Goats");
    private final Band TEST_BAND2 = new Band(2L, "The Extra Glenns");
    private final Band TEST_BAND3 = new Band(3L, "The Congress");

    private final List<Band> BANDS = List.of(TEST_BAND1, TEST_BAND2, TEST_BAND3);

    @BeforeEach
    void setup() {
        this.repo.saveAll(BANDS);
    }

    @Test
    void createTest() throws Exception {
        assertThat(this.service.create(TEST_BAND1)).isEqualTo(this.mapToDTO(TEST_BAND1));
    }

    @Test
    void readOneTest() throws Exception {
        assertThat(this.service.read(TEST_BAND1.getId())).isEqualTo(this.mapToDTO(TEST_BAND1));
    }

    @Test
    void readAllTest() throws Exception {
        assertThat(this.service.read().stream().map(this::mapToPOJO)).isEqualTo(BANDS);
    }

    @Test
    void updateTest() throws Exception {
        assertThat(this.service.update(this.mapToDTO(TEST_BAND1), TEST_BAND1.getId()))
                .isEqualTo(this.mapToDTO(TEST_BAND1));
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(TEST_BAND1.getId())).isTrue();
    }

    @Test
    void findByNameTest() throws Exception {
        assertThat(this.service.findByName(TEST_BAND1.getName())).isEqualTo(BANDS.stream().map(this::mapToDTO)
                .filter(e -> e.getName().equals(TEST_BAND1.getName())).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() throws Exception {
        assertThat(this.service.orderByName().stream().map(e -> e.getName())).isEqualTo(BANDS.stream()
                .sorted(Comparator.comparing(Band::getName)).map(e -> e.getName()).collect(Collectors.toList()));
    }

}
