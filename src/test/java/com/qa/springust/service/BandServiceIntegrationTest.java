package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.dto.BandDTO;
import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;

@SpringBootTest
public class BandServiceIntegrationTest {

    @Autowired
    private BandService service;

    @Autowired
    private BandRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    private BandDTO mapToDTO(Band band) {
        return this.modelMapper.map(band, BandDTO.class);
    }

    private final List<GuitaristDTO> guitarists = new ArrayList<>();
    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTO;

    private Long id;

    private final String name = "The Mountain Goats";

    private final String updatedName = "The Extra Glenns";

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testBand = new Band(this.name);
        this.testBandWithId = this.repo.save(this.testBand);
        this.bandDTO = this.mapToDTO(this.testBandWithId);
        this.id = this.testBandWithId.getId();
    }

    @Test
    void createTest() {
        assertThat(this.bandDTO).isEqualTo(this.service.create(this.testBand));
    }

    @Test
    void readOneTest() {
        assertThat(this.bandDTO).isEqualTo(this.service.read(this.id));
    }

    @Test
    void readAllTest() {
        assertThat(Stream.of(this.bandDTO).collect(Collectors.toList())).isEqualTo(this.service.read());
    }

    @Test
    void updateTest() {
        BandDTO oldDTO = new BandDTO(null, this.updatedName, this.guitarists);
        BandDTO newDTO = new BandDTO(this.id, oldDTO.getName(), oldDTO.getGuitarists());

        assertThat(newDTO).isEqualTo(this.service.update(oldDTO, this.id));
    }

    @Test
    void deleteTest() {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
