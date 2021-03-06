package com.qa.springust.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.springust.exception.MusicianNotFoundException;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.persistence.repository.MusicianRepository;
import com.qa.springust.rest.dto.MusicianDTO;
import com.qa.springust.utils.SpringustBeanUtils;

@Service
public class MusicianService {

    private MusicianRepository repo;

    private ModelMapper mapper;

    private MusicianDTO mapToDTO(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    @Autowired
    public MusicianService(MusicianRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public MusicianDTO create(Musician musician) {
        return this.mapToDTO(this.repo.save(musician));
    }

    public List<MusicianDTO> read() {
        return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public MusicianDTO read(Long id) {
        Musician found = this.repo.findById(id).orElseThrow(MusicianNotFoundException::new);
        return this.mapToDTO(found);
    }

    public MusicianDTO update(MusicianDTO musicianDTO, Long id) {
        Musician toUpdate = this.repo.findById(id).orElseThrow(MusicianNotFoundException::new);
        toUpdate.setName(musicianDTO.getName());
        toUpdate.setStrings(musicianDTO.getStrings());
        toUpdate.setType(musicianDTO.getType());
        SpringustBeanUtils.mergeNotNull(musicianDTO, toUpdate);
        return this.mapToDTO(this.repo.save(toUpdate));
    }

    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

    public List<MusicianDTO> findByName(String name) {
        return this.repo.findByName(name).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MusicianDTO> findByStrings(Integer strings) {
        return this.repo.findByStrings(strings).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MusicianDTO> findByType(String type) {
        return this.repo.findByType(type).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MusicianDTO> orderByName() {
        return this.repo.orderByName().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MusicianDTO> orderByStrings() {
        return this.repo.orderByStrings().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MusicianDTO> orderByType() {
        return this.repo.orderByType().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}
