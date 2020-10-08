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
        List<Guitarist> found = this.repo.findAll();
        List<GuitaristDTO> streamed = found.stream().map(this::mapToDTO).collect(Collectors.toList());
        return streamed;
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
        }
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

}
