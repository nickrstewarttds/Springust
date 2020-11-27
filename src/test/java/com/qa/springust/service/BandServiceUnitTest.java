package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.qa.springust.persistence.domain.Band;
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

    private BandDTO mapToDTO(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    private final Band TEST_BAND1 = new Band(1L, "The Mountain Goats");
    private final Band TEST_BAND2 = new Band(2L, "The Extra Glenns");
    private final Band TEST_BAND3 = new Band(3L, "The Congress");

    private final List<Band> BANDS = List.of(TEST_BAND1, TEST_BAND2, TEST_BAND3);

    @Test
    void createTest() throws Exception {
        when(this.repo.save(TEST_BAND1)).thenReturn(TEST_BAND1);
        assertThat(this.service.create(TEST_BAND1)).isEqualTo(this.mapToDTO(TEST_BAND1));
        verify(this.repo, atLeastOnce()).save(TEST_BAND1);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.repo.findById(TEST_BAND1.getId())).thenReturn(Optional.of(TEST_BAND1));
        assertThat(this.service.read(TEST_BAND1.getId())).isEqualTo(this.mapToDTO(TEST_BAND1));
        verify(this.repo, atLeastOnce()).findById(TEST_BAND1.getId());
    }

    @Test
    void readOneWrongIdTest() throws Exception {
        when(this.repo.findById(TEST_BAND1.getId() + 10)).thenThrow(new BandNotFoundException());
        assertThrows(BandNotFoundException.class, () -> this.service.read(TEST_BAND1.getId() + 10));
    }

    @Test
    void readAllTest() throws Exception {
        when(this.repo.findAll()).thenReturn(BANDS);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() throws Exception {
        when(this.repo.findById(TEST_BAND1.getId())).thenReturn(Optional.of(TEST_BAND1));
        when(this.repo.save(TEST_BAND1)).thenReturn(TEST_BAND1);
        assertThat(this.service.update(this.mapToDTO(TEST_BAND1), TEST_BAND1.getId()))
                .isEqualTo(this.mapToDTO(TEST_BAND1));
        verify(this.repo, atLeastOnce()).findById(TEST_BAND1.getId());
        verify(this.repo, atLeastOnce()).save(TEST_BAND1);
    }

    @Test
    void deleteTest() throws Exception {
        boolean found = false;
        when(this.repo.existsById(TEST_BAND1.getId())).thenReturn(found);
        assertThat(this.service.delete(TEST_BAND1.getId())).isNotEqualTo(found);
        verify(this.repo, atLeastOnce()).existsById(TEST_BAND1.getId());
    }

    @Test
    void findByNameTest() throws Exception {
        when(this.repo.findByName(TEST_BAND1.getName())).thenReturn(BANDS);
        assertThat(this.repo.findByName(TEST_BAND1.getName())).asList().isEqualTo(BANDS);
        verify(this.repo, atLeastOnce()).findByName(TEST_BAND1.getName());
    }

    @Test
    void orderByNameTest() throws Exception {
        when(this.repo.orderByName()).thenReturn(BANDS);
        assertThat(this.repo.orderByName().stream().sorted(Comparator.comparing(Band::getName))
                .collect(Collectors.toList())).isEqualTo(
                        BANDS.stream().sorted(Comparator.comparing(Band::getName)).collect(Collectors.toList()));
        verify(this.repo, atLeastOnce()).orderByName();
    }

}
