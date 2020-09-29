package com.qa.springust.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.springust.dto.BandDTO;
import com.qa.springust.exception.BandNotFoundException;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;
import com.qa.springust.utils.SpringustBeanUtils;

@Service
public class BandService {

    private BandRepository repo;

    private ModelMapper mapper;

    @Autowired
    public BandService(BandRepository repo, ModelMapper mapper) {
        super();
        this.repo = repo;
        this.mapper = mapper;
    }

    private BandDTO mapToDTO(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    private Band mapFromDTO(BandDTO bandDTO) {
        return this.mapper.map(bandDTO, Band.class);
    }

    public BandDTO createBand(BandDTO bandDTO) {
        Band toSave = this.mapFromDTO(bandDTO);
        Band saved = this.repo.save(toSave);
        return this.mapToDTO(saved);
    }

    public List<BandDTO> getAllBands() {
        return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public BandDTO getBandById(Long id) {
        Band found = this.repo.findById(id).orElseThrow(BandNotFoundException::new);
        return this.mapToDTO(found);
    }

    public BandDTO updateBandById(BandDTO bandDTO, Long id) {
        Band toUpdate = this.repo.findById(id).orElseThrow(BandNotFoundException::new);
        SpringustBeanUtils.mergeObject(bandDTO, toUpdate);
        return this.mapToDTO(toUpdate);
    }

    public boolean deleteBandById(Long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

}
