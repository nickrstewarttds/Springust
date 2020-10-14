package com.qa.springust.rest.controller;

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

import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.rest.dto.MusicianDTO;
import com.qa.springust.service.MusicianService;

@SpringBootTest
class MusicianControllerUnitTest {

    // the thing we're actually testing
    // (this is the real thing we've made)
    @Autowired
    private MusicianController controller;

    // the mock thing that we're connecting to
    // so that any requests we receive are always valid
    @MockBean
    private MusicianService service;

    // we need the mapper, because it works with the mock service layer
    @Autowired
    private ModelMapper modelMapper;

    // and we need the dto mapping as well, otherwise we can't test
    // our controller methods (which rely on RE<xDTO>)
    private MusicianDTO mapToDTO(Musician musician) {
        return this.modelMapper.map(musician, MusicianDTO.class);
    }

    private final Long id = 1L;
    private List<Musician> musicianList;
    private Musician testMusician;
    private Musician testMusicianWithId;
    private MusicianDTO musicianDTO;
    private MusicianDTO musicianDTOWithId;

    @BeforeEach
    void init() {
        this.musicianList = new ArrayList<>();

        this.testMusician = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
                MUSICIAN.GUITARIST.getType());
        this.testMusicianWithId = new Musician(this.testMusician.getName(), this.testMusician.getStrings(),
                this.testMusician.getType());
        this.testMusicianWithId.setId(this.id);

        this.musicianList.add(this.testMusicianWithId);

        this.musicianDTO = new MusicianDTO(null, this.testMusician.getName(), this.testMusician.getStrings(),
                this.testMusician.getType());
        this.musicianDTOWithId = this.mapToDTO(this.testMusicianWithId);
    }

    @Test
    void createTest() throws Exception {
        // set up what the mock is doing
        // when running some method, return some value we've predefined up there ^
        when(this.service.create(this.testMusician)).thenReturn(this.musicianDTO);

        // these are the same thing:
        // JUNIT: assertEquals(expected, actual)
        // MOCKITO: assertThat(expected).isEqualTo(actual);
        // .isEqualTo(what is the method actually returning?)
        // assertThat(what do we want to compare the method to?)
        assertThat(new ResponseEntity<MusicianDTO>(this.musicianDTO, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(this.testMusician));

        // check that the mocked method we ran our assertion on ... actually ran!
        verify(this.service, times(1)).create(this.testMusician);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.service.read(this.id)).thenReturn(this.musicianDTO);

        assertThat(new ResponseEntity<MusicianDTO>(this.musicianDTO, HttpStatus.OK))
                .isEqualTo(this.controller.read(this.id));

        verify(this.service, times(1)).read(this.id);
    }

    // controller <- service
    @Test
    void readAllTest() throws Exception {
        when(this.service.read())
                .thenReturn(this.musicianList.stream().map(this::mapToDTO).collect(Collectors.toList()));

        // getBody() = get the list returned from the controller.read() method
        // isEmpty()).isFalse() - check that that list HAS SOMETHING IN IT
        // we can reason that if the list has something in it, it has a guitarist
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();

        verify(this.service, times(1)).read();
    }

    // controller <- service
    @Test
    void updateTest() throws Exception {
        // we need to feed the mocked service some updated data values
        // that way we can test if our old musician changes its values to something else

        // feed the mock service the values we made up here ^
        when(this.service.update(this.musicianDTO, this.id)).thenReturn(this.musicianDTOWithId);

        assertThat(new ResponseEntity<MusicianDTO>(this.musicianDTOWithId, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, this.musicianDTO));

        verify(this.service, times(1)).update(this.musicianDTO, this.id);
    }

    // controller -> service
    @Test
    void deleteTest() throws Exception {
        this.controller.delete(this.id); // this will ping the service, which is mocked!

        // if the delete function ran, then it pinged the service successfully
        // since our service is a mocked one, we don't need to test anything in it
        // therefore: check if the controller delete function runs
        // if it does, then the test passes
        verify(this.service, times(1)).delete(this.id);
    }

}