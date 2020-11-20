package com.qa.springust.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.qa.springust.global.MUSICIAN;
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

    private final Long TEST_ID = 1L;

    private final Musician TEST_GUITARIST = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());
    private final Musician TEST_SAXOPHONIST = new Musician(TEST_ID + 1, MUSICIAN.SAXOPHONIST.getName(),
            MUSICIAN.SAXOPHONIST.getStrings(), MUSICIAN.SAXOPHONIST.getType());
    private final Musician TEST_BASSIST = new Musician(TEST_ID + 2, MUSICIAN.BASSIST.getName(),
            MUSICIAN.BASSIST.getStrings(), MUSICIAN.BASSIST.getType());
    private final Musician TEST_DRUMMER = new Musician(TEST_ID + 3, MUSICIAN.DRUMMER.getName(),
            MUSICIAN.DRUMMER.getStrings(), MUSICIAN.DRUMMER.getType());

    @Test
    void createTest() throws Exception {
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);

        when(this.service.create(TEST_GUITARIST)).thenReturn(expected);
        assertThat(new ResponseEntity<MusicianDTO>(expected, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(TEST_GUITARIST));
        verify(this.service, atLeastOnce()).create(TEST_GUITARIST);
    }

    @Test
    void readOneTest() throws Exception {
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);

        when(this.service.read(TEST_ID)).thenReturn(expected);
        assertThat(new ResponseEntity<MusicianDTO>(expected, HttpStatus.OK)).isEqualTo(this.controller.read(TEST_ID));
        verify(this.service, atLeastOnce()).read(TEST_ID);
    }

    @Test
    void readAllTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_GUITARIST);

        when(this.service.read()).thenReturn(musicians.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, atLeastOnce()).read();
    }

    @Test
    void updateTest() throws Exception {
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);

        when(this.service.update(expected, TEST_ID)).thenReturn(expected);
        assertThat(new ResponseEntity<MusicianDTO>(expected, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(TEST_ID, expected));
        verify(this.service, atLeastOnce()).update(expected, TEST_ID);
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(TEST_ID);
        verify(this.service, atLeastOnce()).delete(TEST_ID);
    }

    @Test
    void findByNameTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.mapToDTO(TEST_GUITARIST));
        musicians.add(this.mapToDTO(TEST_SAXOPHONIST));
        musicians.add(this.mapToDTO(TEST_BASSIST));
        musicians.add(this.mapToDTO(TEST_DRUMMER));

        when(this.service.findByName(TEST_GUITARIST.getName())).thenReturn(musicians);
        assertThat(this.controller.findByName(TEST_GUITARIST.getName()))
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(musicians, HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByName(TEST_GUITARIST.getName());
    }

    @Test
    void findByStringsTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.mapToDTO(TEST_GUITARIST));
        musicians.add(this.mapToDTO(TEST_SAXOPHONIST));
        musicians.add(this.mapToDTO(TEST_BASSIST));
        musicians.add(this.mapToDTO(TEST_DRUMMER));

        when(this.service.findByStrings(TEST_GUITARIST.getStrings())).thenReturn(musicians);
        assertThat(this.controller.findByStrings(TEST_GUITARIST.getStrings()))
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(musicians, HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByStrings(TEST_GUITARIST.getStrings());
    }

    @Test
    void findByTypeTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.mapToDTO(TEST_GUITARIST));
        musicians.add(this.mapToDTO(TEST_SAXOPHONIST));
        musicians.add(this.mapToDTO(TEST_BASSIST));
        musicians.add(this.mapToDTO(TEST_DRUMMER));

        when(this.service.findByType(TEST_GUITARIST.getType())).thenReturn(musicians);
        assertThat(this.controller.findByType(TEST_GUITARIST.getType()))
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(musicians, HttpStatus.OK));
        verify(this.service, atLeastOnce()).findByType(TEST_GUITARIST.getType());
    }

    @Test
    void orderByNameTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.mapToDTO(TEST_GUITARIST));
        musicians.add(this.mapToDTO(TEST_DRUMMER));
        musicians.add(this.mapToDTO(TEST_SAXOPHONIST));
        musicians.add(this.mapToDTO(TEST_BASSIST));

        when(this.service.orderByName()).thenReturn(musicians);
        assertThat(this.controller.orderByName())
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(musicians, HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByName();
    }

    @Test
    void orderByStringsTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.mapToDTO(TEST_DRUMMER));
        musicians.add(this.mapToDTO(TEST_SAXOPHONIST));
        musicians.add(this.mapToDTO(TEST_BASSIST));
        musicians.add(this.mapToDTO(TEST_GUITARIST));

        when(this.service.orderByStrings()).thenReturn(musicians);
        assertThat(this.controller.orderByStrings())
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(musicians, HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByStrings();
    }

    @Test
    void orderByTypeTest() throws Exception {
        List<MusicianDTO> musicians = new ArrayList<>();
        musicians.add(this.mapToDTO(TEST_BASSIST));
        musicians.add(this.mapToDTO(TEST_DRUMMER));
        musicians.add(this.mapToDTO(TEST_GUITARIST));
        musicians.add(this.mapToDTO(TEST_SAXOPHONIST));

        when(this.service.orderByType()).thenReturn(musicians);
        assertThat(this.controller.orderByType())
                .isEqualTo(new ResponseEntity<List<MusicianDTO>>(musicians, HttpStatus.OK));
        verify(this.service, atLeastOnce()).orderByType();
    }

}