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

import com.qa.springust.global.BAND;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;
import com.qa.springust.rest.dto.BandDTO;
import com.qa.springust.rest.dto.MusicianDTO;

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

    private Long id;

    private List<MusicianDTO> musiciansList;

    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTO;
    private BandDTO bandDTOWithId;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.musiciansList = new ArrayList<>();
        this.testBand = new Band(BAND.TMG.getName());
        this.testBandWithId = this.repo.save(this.testBand);
        this.id = this.testBandWithId.getId();
        this.bandDTOWithId = this.mapToDTO(this.testBandWithId);
        this.bandDTO = new BandDTO(null, this.testBand.getName(), this.musiciansList);
    }

    @Test
    void createTest() throws Exception {
        assertThat(this.bandDTOWithId).isEqualTo(this.service.create(this.testBand));
    }

    @Test
    void readOneTest() throws Exception {
        assertThat(this.bandDTOWithId).isEqualTo(this.service.read(this.id));
    }

    @Test
    void readAllTest() throws Exception {
        assertThat(Stream.of(this.bandDTOWithId).collect(Collectors.toList())).isEqualTo(this.service.read());
    }

    @Test
    void updateTest() throws Exception {
        assertThat(this.bandDTOWithId).isEqualTo(this.service.update(this.bandDTO, this.id));
    }

    @Test
    void deleteTest() throws Exception {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
