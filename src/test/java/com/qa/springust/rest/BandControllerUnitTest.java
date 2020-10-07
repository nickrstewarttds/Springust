package com.qa.springust.rest;

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

import com.qa.springust.dto.BandDTO;
import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.persistence.domain.Band;
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

    private List<Band> bandList;
    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTO;

    private final Long id = 1L;
    private final String name = "The Mountain Goats";

    @BeforeEach
    void init() {
        this.bandList = new ArrayList<>();
        this.testBand = new Band(name);
        this.testBandWithId = new Band(testBand.getName());
        this.testBandWithId.setId(id);
        this.bandList.add(this.testBandWithId);
    }

    @Test
    void createTest() throws Exception {
        when(this.service.create(this.testBand)).thenReturn(this.bandDTO);
        assertThat(new ResponseEntity<BandDTO>(this.bandDTO, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(this.testBand));
        verify(this.service, times(1)).create(this.testBand);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.service.read(this.id)).thenReturn(this.bandDTO);
        assertThat(new ResponseEntity<BandDTO>(this.bandDTO, HttpStatus.OK)).isEqualTo(this.controller.read(this.id));
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
        final List<GuitaristDTO> GUITARISTS = new ArrayList<>();
        BandDTO oldBand = new BandDTO(null, "The Mountain Goats", GUITARISTS);
        BandDTO newBand = new BandDTO(this.id, oldBand.getName(), oldBand.getGuitarists());

        when(this.service.update(oldBand, this.id)).thenReturn(newBand);
        assertThat(new ResponseEntity<BandDTO>(newBand, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, oldBand));
        verify(this.service, times(1)).update(oldBand, this.id);
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(this.id);
        verify(this.service, times(1)).delete(this.id);
    }
}
