package com.qa.springust.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.persistence.domain.Guitarist;
import com.qa.springust.service.GuitaristService;

@SpringBootTest
class GuitaristControllerUnitTest {

    @Autowired
    private GuitaristController controller;
    
    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private GuitaristService service;

    private List<Guitarist> guitaristList;
    private Guitarist testGuitarist;
    private Guitarist testGuitaristWithId;
    private GuitaristDTO guitaristDTO;
    private final Long id = 1L;

    private GuitaristDTO mapToDTO(Guitarist guitarist) {
        return this.modelMapper.map(guitarist, GuitaristDTO.class);
    }

    @BeforeEach
    void init() {
        this.guitaristList = new ArrayList<>();
        this.testGuitarist = new Guitarist("John Darnielle", 6, "Ibanez Talman");
        this.testGuitaristWithId = new Guitarist(testGuitarist.getName(), testGuitarist.getStrings(), testGuitarist.getType());
        this.testGuitaristWithId.setId(id);
        this.guitaristList.add(testGuitaristWithId);
        this.guitaristDTO = this.mapToDTO(testGuitaristWithId);
    }

    @Test
    void createTest() {
        when(this.service.create(testGuitarist))
            .thenReturn(this.guitaristDTO);
        
        assertThat(new ResponseEntity<GuitaristDTO>(this.guitaristDTO, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(testGuitarist));
        
        verify(this.service, times(1))
            .create(this.testGuitarist);
    }

    @Test
    void readTest() {
        when(this.service.read(this.id))
            .thenReturn(this.guitaristDTO);
        
        assertThat(new ResponseEntity<GuitaristDTO>(this.guitaristDTO, HttpStatus.OK))
                .isEqualTo(this.controller.read(this.id));
        
        verify(this.service, times(1))
            .read(this.id);
    }

    @Test
    void readAllTest() {
        when(service.read())
            .thenReturn(this.guitaristList
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList()));
        
        assertThat(this.controller.read().getBody()
                .isEmpty()).isFalse();
        
        verify(service, times(1))
            .read();
    }

    @Test
    void updateTest() {
        // given
        GuitaristDTO newGuitarist = new GuitaristDTO(null, "Peter Peter Hughes", 4, "Fender American");
        GuitaristDTO updatedGuitarist = new GuitaristDTO(this.id, newGuitarist.getName(), newGuitarist.getStrings(),
                newGuitarist.getType());

        when(this.service.update(newGuitarist, this.id))
            .thenReturn(updatedGuitarist);
        
        assertThat(new ResponseEntity<GuitaristDTO>(updatedGuitarist, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, newGuitarist));
        
        verify(this.service, times(1))
            .update(newGuitarist, this.id);
    }
    
    @Test
    void deleteTest() {
        this.controller.delete(id);

        verify(this.service, times(1))
            .delete(id);
    }

}