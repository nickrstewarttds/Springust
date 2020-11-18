package com.qa.springust.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
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

    private BandDTO map(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Band TEST_BAND = new Band(TEST_ID, BAND.TMG.getName());

    @Test
    void createTest() throws Exception {
        BandDTO expected = this.map(TEST_BAND);

        when(this.service.create(TEST_BAND)).thenReturn(expected);
        assertThat(new ResponseEntity<BandDTO>(expected, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(TEST_BAND));
        verify(this.service, times(1)).create(TEST_BAND);
    }

    @Test
    void readOneTest() throws Exception {
        BandDTO expected = this.map(TEST_BAND);

        when(this.service.read(TEST_ID)).thenReturn(expected);
        assertThat(new ResponseEntity<BandDTO>(expected, HttpStatus.OK)).isEqualTo(this.controller.read(TEST_ID));
        verify(this.service, times(1)).read(TEST_ID);
    }

    @Test
    void readAllTest() throws Exception {
        List<Band> bands = new ArrayList<>();
        bands.add(TEST_BAND);

        when(this.service.read()).thenReturn(bands.stream().map(this::map).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, times(1)).read();
    }

    @Test
    void updateTest() throws Exception {
        BandDTO expected = this.map(TEST_BAND);

        when(this.service.update(expected, TEST_ID)).thenReturn(expected);
        assertThat(new ResponseEntity<BandDTO>(expected, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(TEST_ID, expected));
        verify(this.service, times(1)).update(expected, TEST_ID);
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(TEST_ID);
        verify(this.service, times(1)).delete(TEST_ID);
    }
}
