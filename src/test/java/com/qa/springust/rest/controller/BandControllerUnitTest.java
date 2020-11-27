package com.qa.springust.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private final Band TEST_BAND1 = new Band(1L, "The Mountain Goats");
    private final Band TEST_BAND2 = new Band(2L, "The Extra Glenns");
    private final Band TEST_BAND3 = new Band(3L, "The Congress");

    private final List<Band> BANDS = List.of(TEST_BAND1, TEST_BAND2, TEST_BAND3);

    @Test
    void createTest() throws Exception {
        when(this.service.create(TEST_BAND1)).thenReturn(this.mapToDTO(TEST_BAND1));
        assertThat(this.controller.create(TEST_BAND1))
                .isEqualTo(new ResponseEntity<BandDTO>(this.mapToDTO(TEST_BAND1), HttpStatus.CREATED));
        verify(this.service, atLeastOnce()).create(TEST_BAND1);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.service.read(TEST_BAND1.getId())).thenReturn(this.mapToDTO(TEST_BAND1));
        assertThat(this.controller.read(TEST_BAND1.getId()))
                .isEqualTo(new ResponseEntity<BandDTO>(this.mapToDTO(TEST_BAND1), HttpStatus.OK));
        verify(this.service, atLeastOnce()).read(TEST_BAND1.getId());
    }

    @Test
    void readAllTest() throws Exception {
        when(this.service.read()).thenReturn(BANDS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, atLeastOnce()).read();
    }

    @Test
    void updateTest() throws Exception {
        when(this.service.update(this.mapToDTO(TEST_BAND1), TEST_BAND1.getId())).thenReturn(this.mapToDTO(TEST_BAND1));
        assertThat(this.controller.update(TEST_BAND1.getId(), this.mapToDTO(TEST_BAND1)))
                .isEqualTo(new ResponseEntity<BandDTO>(this.mapToDTO(TEST_BAND1), HttpStatus.ACCEPTED));
        verify(this.service, atLeastOnce()).update(this.mapToDTO(TEST_BAND1), TEST_BAND1.getId());
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(TEST_BAND1.getId());
        verify(this.service, atLeastOnce()).delete(TEST_BAND1.getId());
    }

    @Test
    void findByNameTest() throws Exception {
        when(this.service.findByName(TEST_BAND1.getName()))
                .thenReturn(BANDS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.findByName(TEST_BAND1.getName())).isEqualTo(new ResponseEntity<List<BandDTO>>(
                BANDS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByName(TEST_BAND1.getName());
    }

    @Test
    void orderByNameTest() throws Exception {
        when(this.service.orderByName()).thenReturn(BANDS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.orderByName()).isEqualTo(new ResponseEntity<List<BandDTO>>(
                BANDS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByName();

    }
}
