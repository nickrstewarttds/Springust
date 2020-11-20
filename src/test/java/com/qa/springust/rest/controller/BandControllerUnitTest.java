package com.qa.springust.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.qa.springust.global.BAND;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.rest.dto.BandDTO;
import com.qa.springust.service.BandService;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class BandControllerUnitTest {

    @Autowired
    private BandController controller;

    @MockBean
    private BandService service;

    @Autowired
    private ModelMapper mapper;

    private BandDTO mapToDTO(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Band TEST_BAND1 = new Band(TEST_ID, BAND.TMG.getName());
    private final Band TEST_BAND2 = new Band(TEST_ID + 1, BAND.TEG.getName());
    private final Band TEST_BAND3 = new Band(TEST_ID + 2, BAND.TEL.getName());

    @Test
    void createTest() throws Exception {
        BandDTO expected = this.mapToDTO(TEST_BAND1);

        when(this.service.create(TEST_BAND1)).thenReturn(expected);
        assertThat(this.controller.create(TEST_BAND1))
                .isEqualTo(new ResponseEntity<BandDTO>(expected, HttpStatus.CREATED));
        verify(this.service, atLeastOnce()).create(TEST_BAND1);
    }

    @Test
    void readOneTest() throws Exception {
        BandDTO expected = this.mapToDTO(TEST_BAND1);

        when(this.service.read(TEST_ID)).thenReturn(expected);
        assertThat(this.controller.read(TEST_ID)).isEqualTo(new ResponseEntity<BandDTO>(expected, HttpStatus.OK));
        verify(this.service, atLeastOnce()).read(TEST_ID);
    }

    @Test
    void readAllTest() throws Exception {
        List<Band> bands = new ArrayList<>();
        bands.add(TEST_BAND1);

        when(this.service.read()).thenReturn(bands.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, atLeastOnce()).read();
    }

    @Test
    void updateTest() throws Exception {
        BandDTO expected = this.mapToDTO(TEST_BAND1);

        when(this.service.update(expected, TEST_ID)).thenReturn(expected);
        assertThat(this.controller.update(TEST_ID, expected))
                .isEqualTo(new ResponseEntity<BandDTO>(expected, HttpStatus.ACCEPTED));
        verify(this.service, atLeastOnce()).update(expected, TEST_ID);
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(TEST_ID);
        verify(this.service, atLeastOnce()).delete(TEST_ID);
    }

    @Test
    void findByNameTest() throws Exception {
        List<BandDTO> bands = new ArrayList<>();
        bands.add(this.mapToDTO(TEST_BAND1));
        bands.add(this.mapToDTO(TEST_BAND2));
        bands.add(this.mapToDTO(TEST_BAND3));

        when(this.service.findByName(TEST_BAND1.getName())).thenReturn(bands);
        assertThat(this.controller.findByName(TEST_BAND1.getName()))
                .isEqualTo(new ResponseEntity<List<BandDTO>>(bands, HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByName(TEST_BAND1.getName());
    }

    @Test
    void orderByNameTest() throws Exception {
        List<BandDTO> bands = new ArrayList<>();
        bands.add(this.mapToDTO(TEST_BAND2));
        bands.add(this.mapToDTO(TEST_BAND3));
        bands.add(this.mapToDTO(TEST_BAND1));

        when(this.service.orderByName()).thenReturn(bands);
        assertThat(this.controller.orderByName()).isEqualTo(new ResponseEntity<List<BandDTO>>(bands, HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByName();

    }
}
