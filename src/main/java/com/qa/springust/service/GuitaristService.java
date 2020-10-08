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
        this.repo = repo;
        this.mapper = mapper;
    }

    private GuitaristDTO mapToDTO(Guitarist guitarist) {
        return this.mapper.map(guitarist, GuitaristDTO.class);
    }

    public GuitaristDTO create(Guitarist guitarist) {
        Guitarist created = this.repo.save(guitarist);
        GuitaristDTO mapped = this.mapToDTO(created);
        return mapped;
    }

    public List<GuitaristDTO> read() {
        return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public GuitaristDTO read(Long id) {
        Guitarist found = this.repo.findById(id).orElseThrow(GuitaristNotFoundException::new);
        return this.mapToDTO(found);
    }

    public GuitaristDTO update(GuitaristDTO guitaristDTO, Long id) {
        Guitarist toUpdate = this.repo.findById(id).orElseThrow(GuitaristNotFoundException::new);
        toUpdate.setName(guitaristDTO.getName());
        toUpdate.setStrings(guitaristDTO.getStrings());
        toUpdate.setType(guitaristDTO.getType());
        SpringustBeanUtils.mergeNotNull(guitaristDTO, toUpdate);
        return this.mapToDTO(this.repo.save(toUpdate));
    }

    public boolean delete(Long id) {
        if (!this.repo.existsById(id)) {
            throw new GuitaristNotFoundException();
        } else {
            this.repo.deleteById(id);
        }
        return !this.repo.existsById(id);
    }

    public List<GuitaristDTO> findByName(String name) {
        return this.repo.findByName(name).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<GuitaristDTO> findByStrings(Integer strings) {
        return this.repo.findByStrings(strings).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<GuitaristDTO> findByType(String type) {
        return this.repo.findByType(type).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<GuitaristDTO> orderByNameAZ() {
        return this.repo.orderByName().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<GuitaristDTO> orderByStringsAsc() {
        return this.repo.orderByStrings().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<GuitaristDTO> orderByTypeAZ() {
        return this.repo.orderByType().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}
