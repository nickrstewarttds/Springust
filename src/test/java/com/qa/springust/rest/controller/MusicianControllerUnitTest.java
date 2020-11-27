package com.qa.springust.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.rest.dto.MusicianDTO;
import com.qa.springust.service.MusicianService;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class MusicianControllerUnitTest {

    @Autowired
    private MusicianController controller;

    @MockBean
    private MusicianService service;

    @Autowired
    private ModelMapper mapper;

    private MusicianDTO mapToDTO(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private final Musician TEST_GUITARIST = new Musician(1L, "John Darnielle", 6, "guitarist");
    private final Musician TEST_SAXOPHONIST = new Musician(2L, "Matt Douglas", 0, "saxophonist");
    private final Musician TEST_BASSIST = new Musician(3L, "Peter Hughes", 4, "bassist");
    private final Musician TEST_DRUMMER = new Musician(4L, "Jon Wurster", 0, "drummer");

    private final List<Musician> MUSICIANS = List.of(TEST_GUITARIST, TEST_SAXOPHONIST, TEST_BASSIST, TEST_DRUMMER);

    @Test
    void createTest() throws Exception {
        when(this.service.create(TEST_GUITARIST)).thenReturn(this.mapToDTO(TEST_GUITARIST));
        assertThat(new ResponseEntity<MusicianDTO>(this.mapToDTO(TEST_GUITARIST), HttpStatus.CREATED))
                .isEqualTo(this.controller.create(TEST_GUITARIST));
        verify(this.service, atLeastOnce()).create(TEST_GUITARIST);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.service.read(TEST_GUITARIST.getId())).thenReturn(this.mapToDTO(TEST_GUITARIST));
        assertThat(new ResponseEntity<MusicianDTO>(this.mapToDTO(TEST_GUITARIST), HttpStatus.OK))
                .isEqualTo(this.controller.read(TEST_GUITARIST.getId()));
        verify(this.service, atLeastOnce()).read(TEST_GUITARIST.getId());
    }

    @Test
    void readAllTest() throws Exception {
        when(this.service.read()).thenReturn(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, atLeastOnce()).read();
    }

    @Test
    void updateTest() throws Exception {
        when(this.service.update(this.mapToDTO(TEST_GUITARIST), TEST_GUITARIST.getId()))
                .thenReturn(this.mapToDTO(TEST_GUITARIST));
        assertThat(new ResponseEntity<MusicianDTO>(this.mapToDTO(TEST_GUITARIST), HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(TEST_GUITARIST.getId(), this.mapToDTO(TEST_GUITARIST)));
        verify(this.service, atLeastOnce()).update(this.mapToDTO(TEST_GUITARIST), TEST_GUITARIST.getId());
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(TEST_GUITARIST.getId());
        verify(this.service, atLeastOnce()).delete(TEST_GUITARIST.getId());
    }

    @Test
    void findByNameTest() throws Exception {
        when(this.service.findByName(TEST_GUITARIST.getName()))
                .thenReturn(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.findByName(TEST_GUITARIST.getName()))
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(
                        MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByName(TEST_GUITARIST.getName());
    }

    @Test
    void findByStringsTest() throws Exception {
        when(this.service.findByStrings(TEST_GUITARIST.getStrings()))
                .thenReturn(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.findByStrings(TEST_GUITARIST.getStrings()))
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(
                        MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByStrings(TEST_GUITARIST.getStrings());
    }

    @Test
    void findByTypeTest() throws Exception {
        when(this.service.findByType(TEST_GUITARIST.getType()))
                .thenReturn(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.findByType(TEST_GUITARIST.getType()))
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(
                        MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByType(TEST_GUITARIST.getType());
    }

    @Test
    void orderByNameTest() throws Exception {
        when(this.service.orderByName())
                .thenReturn(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.orderByName()).isEqualTo(new ResponseEntity<List<MusicianDTO>>(
                MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByName();
    }

    @Test
    void orderByStringsTest() throws Exception {
        when(this.service.orderByStrings())
                .thenReturn(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.orderByStrings()).isEqualTo(new ResponseEntity<List<MusicianDTO>>(
                MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByStrings();
    }

    @Test
    void orderByTypeTest() throws Exception {
        when(this.service.orderByType())
                .thenReturn(MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.orderByType()).isEqualTo(new ResponseEntity<List<MusicianDTO>>(
                MUSICIANS.stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByType();
    }

}