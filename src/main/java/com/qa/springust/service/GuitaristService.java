package com.qa.springust.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.exception.GuitaristNotFoundException;
import com.qa.springust.persistence.domain.Guitarist;
import com.qa.springust.persistence.repository.GuitaristRepository;
import com.qa.springust.utils.SpringustBeanUtils;

@Service
public class GuitaristService {

    private GuitaristRepository repo;

    private ModelMapper mapper;

    @Autowired
    public GuitaristService(GuitaristRepository repo, ModelMapper mapper) {
        super();
        this.repo = repo;
        this.mapper = mapper;
    }

    private GuitaristDTO mapToDTO(Guitarist guitarist) {
        return this.mapper.map(guitarist, GuitaristDTO.class);
    }

    private Guitarist mapFromDTO(GuitaristDTO guitaristDTO) {
        return this.mapper.map(guitaristDTO, Guitarist.class);
    }

    // create
    public GuitaristDTO createGuitarist(GuitaristDTO guitaristDTO) {
        Guitarist toSave = this.mapFromDTO(guitaristDTO);
        Guitarist saved = this.repo.save(toSave);
        return this.mapToDTO(saved);
    }

    // readAll
    public List<GuitaristDTO> getAllGuitarists() {
        return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // readById
    public GuitaristDTO getGuitaristById(Long id) {
        Guitarist found = this.repo.findById(id).orElseThrow(GuitaristNotFoundException::new);
        return this.mapToDTO(found);
    }

    // update
    public GuitaristDTO updateGuitaristById(GuitaristDTO guitaristDTO, Long id) {
        Guitarist toUpdate = this.repo.findById(id).orElseThrow(GuitaristNotFoundException::new);
        SpringustBeanUtils.mergeObject(guitaristDTO, toUpdate);
        return this.mapToDTO(this.repo.save(toUpdate));
    }

    // delete
    public boolean deleteGuitaristById(Long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

}
