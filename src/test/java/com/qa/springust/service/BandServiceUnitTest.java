package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.qa.springust.exception.BandNotFoundException;
import com.qa.springust.global.BAND;
import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.persistence.repository.BandRepository;
import com.qa.springust.rest.dto.BandDTO;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class BandServiceUnitTest {
    @Autowired
    private BandService service;

    @MockBean
    private BandRepository repo;

    @Autowired
    private ModelMapper mapper;

    private BandDTO map(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Musician TEST_MUSICIAN = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
            MUSICIAN.GUITARIST.getType());

    private final Band TEST_BAND1 = new Band(BAND.TMG.getName());
    private final Band TEST_BAND2 = new Band(TEST_ID + 1, BAND.TEG.getName());
    private final Band TEST_BAND3 = new Band(TEST_ID + 2, BAND.TEL.getName());

    @Test
    void createTest() throws Exception {
        TEST_BAND1.setId(TEST_ID);
        BandDTO expected = this.map(TEST_BAND1);

        when(this.repo.save(TEST_BAND1)).thenReturn(TEST_BAND1);
        assertThat(this.service.create(TEST_BAND1)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).save(TEST_BAND1);
    }

    @Test
    void readOneTest() throws Exception {
        TEST_BAND1.setId(TEST_ID);
        BandDTO expected = this.map(TEST_BAND1);

        when(this.repo.findById(TEST_ID)).thenReturn(Optional.of(TEST_BAND1));
        assertThat(this.service.read(TEST_ID)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).findById(TEST_ID);
    }

    @Test
    void readOneWrongIdTest() throws Exception {
        List<Band> bands = new ArrayList<>();
        TEST_BAND1.setId(TEST_ID);
        bands.add(TEST_BAND1);

        when(this.repo.findById(TEST_ID)).thenThrow(new BandNotFoundException());
        assertThrows(BandNotFoundException.class, () -> this.service.read(TEST_ID));
    }

    @Test
    void readAllTest() throws Exception {
        List<Band> bands = new ArrayList<>();
        TEST_BAND1.setId(TEST_ID);
        bands.add(TEST_BAND1);

        when(this.repo.findAll()).thenReturn(bands);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() throws Exception {
        TEST_MUSICIAN.setId(TEST_ID);
        BandDTO expected = this.map(TEST_BAND1);

        when(this.repo.findById(TEST_ID)).thenReturn(Optional.of(TEST_BAND1));
        when(this.repo.save(TEST_BAND1)).thenReturn(TEST_BAND1);
        assertThat(this.service.update(expected, TEST_ID)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).findById(TEST_ID);
        verify(this.repo, atLeastOnce()).save(TEST_BAND1);
    }

    @Test
    void deleteTest() throws Exception {
        boolean found = false;

        when(this.repo.existsById(TEST_ID)).thenReturn(!found, found);
        assertThat(this.service.delete(TEST_ID)).isNotEqualTo(found);
        verify(this.repo, atLeastOnce()).deleteById(TEST_ID);
        verify(this.repo, atLeastOnce()).existsById(TEST_ID);
    }

    @Test
    void deleteWrongIDTest() {
        boolean found = false;

        when(this.repo.existsById(TEST_ID)).thenReturn(found);
        assertThrows(BandNotFoundException.class, () -> this.service.delete(TEST_ID));
        verify(this.repo, atLeastOnce()).existsById(TEST_ID);
    }

    @Test
    void findByNameTest() {
        List<Band> bands = new ArrayList<>();
        TEST_BAND1.setId(TEST_ID);
        bands.add(TEST_BAND1);

        when(this.repo.findByName(TEST_BAND1.getName())).thenReturn(bands);
        assertThat(this.repo.findByName(TEST_BAND1.getName())).asList().isEqualTo(bands);
        verify(this.repo, atLeastOnce()).findByName(TEST_BAND1.getName());
    }

    @Test
    void orderByNameTest() {
        List<Band> bands = new ArrayList<>();
        TEST_BAND1.setId(TEST_ID);
        bands.add(TEST_BAND2);
        bands.add(TEST_BAND3);
        bands.add(TEST_BAND1);

        when(this.repo.orderByName()).thenReturn(bands);
        assertThat(this.repo.orderByName().stream().sorted(Comparator.comparing(Band::getName))
                .collect(Collectors.toList())).isEqualTo(bands);
        verify(this.repo, atLeastOnce()).orderByName();
    }

}
