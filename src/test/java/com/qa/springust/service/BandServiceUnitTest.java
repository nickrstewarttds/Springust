package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.springust.global.BAND;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;
import com.qa.springust.rest.dto.BandDTO;
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
public class BandServiceUnitTest {
    @Autowired
    private BandService service;

    @MockBean
    private BandRepository repo;

    @MockBean
    private ModelMapper modelMapper;

    final Long id = 1L;

    private List<Band> bandList;
    private List<MusicianDTO> musicianDTOList;

    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTO;
    private BandDTO bandDTOWithId;

    @BeforeEach
    void init() {
        this.bandList = new ArrayList<>();
        this.musicianDTOList = new ArrayList<>();

        this.testBand = new Band(BAND.TMG.getName());
        this.bandList.add(this.testBand);

        this.testBandWithId = new Band(testBand.getName());
        this.testBandWithId.setId(this.id);

        this.bandDTOWithId = modelMapper.map(testBandWithId, BandDTO.class);

        this.bandDTO = new BandDTO(null, this.testBand.getName(), this.musicianDTOList);
    }

    @Test
    void createTest() throws Exception {
        when(this.repo.save(this.testBand)).thenReturn(this.testBandWithId);
        when(this.modelMapper.map(this.testBandWithId, BandDTO.class)).thenReturn(this.bandDTOWithId);
        assertThat(this.bandDTOWithId).isEqualTo(this.service.create(this.testBand));
        verify(this.repo, atLeastOnce()).save(this.testBand);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testBandWithId));
        when(this.modelMapper.map(this.testBandWithId, BandDTO.class)).thenReturn(this.bandDTOWithId);
        assertThat(this.bandDTOWithId).isEqualTo(this.service.read(this.id));
        verify(this.repo, atLeastOnce()).findById(this.id);
    }

    @Test
    void readAllTest() throws Exception {
        when(this.repo.findAll()).thenReturn(this.bandList);
        when(this.modelMapper.map(this.testBandWithId, BandDTO.class)).thenReturn(this.bandDTOWithId);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() throws Exception {
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testBand));
        when(this.repo.save(this.testBand)).thenReturn(this.testBandWithId);
        when(this.modelMapper.map(this.testBandWithId, BandDTO.class)).thenReturn(this.bandDTOWithId);
        assertThat(this.bandDTOWithId).isEqualTo(this.service.update(this.bandDTO, this.id));
        verify(this.repo, atLeastOnce()).findById(this.id);
        verify(this.repo, atLeastOnce()).save(this.testBand);
    }

    @Test
    void deleteTest() throws Exception {
        when(this.repo.existsById(this.id)).thenReturn(true, false);
        assertThat(this.service.delete(this.id)).isTrue();
        verify(this.repo, atLeastOnce()).deleteById(this.id);
        verify(this.repo, atLeastOnce()).existsById(this.id);
    }

}
