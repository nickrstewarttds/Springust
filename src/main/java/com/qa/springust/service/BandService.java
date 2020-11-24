package com.qa.springust.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.springust.exception.BandNotFoundException;
import com.qa.springust.persistence.domain.Band;
import com.qa.springust.persistence.repository.BandRepository;
import com.qa.springust.rest.dto.BandDTO;
import com.qa.springust.utils.SpringustBeanUtils;

@Service
public class BandService {

    private BandRepository repo;

    private ModelMapper mapper;

    private BandDTO mapToDTO(Band band) {
        return this.mapper.map(band, BandDTO.class);
    }

    @Autowired
    public BandService(BandRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public BandDTO create(Band band) {
        return this.mapToDTO(this.repo.save(band));
    }

    public List<BandDTO> read() {
        return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public BandDTO read(Long id) {
        return this.mapToDTO(this.repo.findById(id).orElseThrow(BandNotFoundException::new));
    }

    public BandDTO update(BandDTO bandDTO, Long id) {
        Band toUpdate = this.repo.findById(id).orElseThrow(BandNotFoundException::new);
        toUpdate.setName(bandDTO.getName());
        SpringustBeanUtils.mergeNotNull(bandDTO, toUpdate);
        return this.mapToDTO(this.repo.save(toUpdate));
    }

    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

    public List<BandDTO> findByName(String name) {
        return this.repo.findByName(name).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<BandDTO> orderByName() {
        return this.repo.orderByName().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}
