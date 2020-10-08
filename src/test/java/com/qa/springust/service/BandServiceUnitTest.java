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

import com.qa.springust.dto.BandDTO;
import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;

@SpringBootTest
public class BandServiceUnitTest {
    @Autowired
    private BandService service;

    @MockBean
    private BandRepository repo;

    @MockBean
    private ModelMapper modelMapper;

    private List<Band> bandList;
    private Band testBand;
    private Band testBandWithId;
    private BandDTO bandDTO;

    final Long id = 1L;

    @BeforeEach
    void init() {
        this.bandList = new ArrayList<>();
        this.testBand = new Band("The Mountain Goats");
        bandList.add(testBand);
        this.testBandWithId = new Band(testBand.getName());
        this.testBandWithId.setId(this.id);
        this.bandDTO = modelMapper.map(testBandWithId, BandDTO.class);
    }

    @Test
    void createTest() {
        when(this.repo.save(this.testBand)).thenReturn(this.testBandWithId);
        when(this.modelMapper.map(this.testBandWithId, BandDTO.class)).thenReturn(this.bandDTO);
        assertThat(this.bandDTO).isEqualTo(this.service.create(this.testBand));
        verify(this.repo, atLeastOnce()).save(this.testBand);
    }

    @Test
    void readOneTest() {
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testBandWithId));
        when(this.modelMapper.map(testBandWithId, BandDTO.class)).thenReturn(this.bandDTO);
        assertThat(this.bandDTO).isEqualTo(this.service.read(this.id));
        verify(this.repo, atLeastOnce()).findById(this.id);
    }

    @Test
    void readAllTest() {
        when(this.repo.findAll()).thenReturn(this.bandList);
        when(this.modelMapper.map(this.testBandWithId, BandDTO.class)).thenReturn(this.bandDTO);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() {
        final List<GuitaristDTO> guitaristList = new ArrayList<>();
        final String name = "The Extra Glenns";

        Band oldEntity = new Band(name);
        oldEntity.setId(this.id);
        BandDTO oldDTO = new BandDTO(null, name, guitaristList);

        Band newEntity = new Band(oldEntity.getName());
        newEntity.setId(this.id);
        BandDTO newDTO = new BandDTO(this.id, newEntity.getName(), oldDTO.getGuitarists());

        when(this.repo.findById(this.id)).thenReturn(Optional.of(oldEntity));
        when(this.repo.save(oldEntity)).thenReturn(newEntity);
        when(this.modelMapper.map(newEntity, BandDTO.class)).thenReturn(newDTO);
        assertThat(newDTO).isEqualTo(this.service.update(oldDTO, this.id));
        verify(this.repo, atLeastOnce()).findById(this.id);
        verify(this.repo, atLeastOnce()).save(newEntity);
    }

    @Test
    void deleteTest() {
        when(this.repo.existsById(this.id)).thenReturn(true, false);
        assertThat(this.service.delete(this.id)).isTrue();
        verify(this.repo, atLeastOnce()).deleteById(this.id);
        verify(this.repo, atLeastOnce()).existsById(this.id);
    }

}
