package com.qa.springust.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.springust.global.BAND;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.rest.dto.BandDTO;
import com.qa.springust.rest.dto.MusicianDTO;
import com.qa.springust.service.BandService;

@SpringBootTest
class BandControllerUnitTest {

    @Autowired
    private BandController controller;

    @MockBean
    private BandService service;

    @Autowired
    private ModelMapper modelMapper;

    private BandDTO mapToDTO(Band band) {
        return this.modelMapper.map(band, BandDTO.class);
    }

    private final Long id = 1L;
    private List<Band> bandList;
    private List<MusicianDTO> guitaristDTOList;
    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTO;
    private BandDTO bandDTOWithId;

    @BeforeEach
    void init() {
        this.bandList = new ArrayList<>();
        this.guitaristDTOList = new ArrayList<>();

        this.testBand = new Band(BAND.TMG.getName());
        this.testBandWithId = new Band(this.testBand.getName());
        this.testBandWithId.setId(this.id);

        this.bandList.add(this.testBandWithId);

        this.bandDTO = new BandDTO(null, this.testBand.getName(), this.guitaristDTOList);
        this.bandDTOWithId = this.mapToDTO(this.testBandWithId);
    }

    @Test
    void createTest() throws Exception {
        when(this.service.create(this.testBand)).thenReturn(this.bandDTOWithId);
        assertThat(new ResponseEntity<BandDTO>(this.bandDTOWithId, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(this.testBand));
        verify(this.service, times(1)).create(this.testBand);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.service.read(this.id)).thenReturn(this.bandDTOWithId);
        assertThat(new ResponseEntity<BandDTO>(this.bandDTOWithId, HttpStatus.OK))
                .isEqualTo(this.controller.read(this.id));
        verify(this.service, times(1)).read(this.id);
    }

    @Test
    void readAllTest() throws Exception {
        when(this.service.read()).thenReturn(this.bandList.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, times(1)).read();
    }

    @Test
    void updateTest() throws Exception {
        when(this.service.update(this.bandDTO, this.id)).thenReturn(this.bandDTOWithId);
        assertThat(new ResponseEntity<BandDTO>(this.bandDTOWithId, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, this.bandDTO));
        verify(this.service, times(1)).update(this.bandDTO, this.id);
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(this.id);
        verify(this.service, times(1)).delete(this.id);
    }
}
