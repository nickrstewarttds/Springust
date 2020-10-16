package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.persistence.repository.MusicianRepository;
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
class MusicianServiceUnitTest {

    // autowiring -> is using something you've already got lying around
    @Autowired
    private MusicianService service;

    // mockbean -> get mockito to simulate responses from whatever class
    @MockBean
    private MusicianRepository repo;

    // we've mocked the modelMapper here, which means we need to set its values
    // for every test we use it with using the when().theReturn() method-chain
    @MockBean
    private ModelMapper modelMapper;

    // we don't need a mapToDTO() function if we're spinning up a mock modelMapper
    // because mockito inverts the control of that object - as in, manages it
    // for us instead :) less code woo

    // final values to assign to those <expected> objects
    final Long id = 1L;

    // items we're spinning up to <expect> from our unit tests
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
        this.musicianList.add(this.testMusician);

        this.testMusicianWithId = new Musician(this.testMusician.getName(), this.testMusician.getStrings(),
                this.testMusician.getType());
        this.testMusicianWithId.setId(this.id);

        this.musicianDTOWithId = this.modelMapper.map(this.testMusicianWithId, MusicianDTO.class);

        this.musicianDTO = new MusicianDTO(null, this.testMusician.getName(), this.testMusician.getStrings(),
                this.testMusician.getType());
    }

    @Test
    void createTest() throws Exception {
        // a when() to set up our mocked repo
        when(this.repo.save(this.testMusician)).thenReturn(this.testMusicianWithId);

        // and a when() to set up our mocked modelMapper
        when(this.modelMapper.map(this.testMusicianWithId, MusicianDTO.class)).thenReturn(this.musicianDTOWithId);

        // check that the guitaristDTO we set up as our <expected> value
        // is the same as the <actual> result we get when running the service.create()
        // method
        assertThat(this.musicianDTOWithId).isEqualTo(this.service.create(this.testMusician));

        // we check that our mocked repository was hit - if it was, that means our
        // service works
        verify(this.repo, atLeastOnce()).save(this.testMusician);
    }

    @Test
    void readOneTest() throws Exception {
        // the repo we spin up extends a Spring type of repo that uses Optionals
        // thus, when we run a method in the repo (e.g. findById() ) we have to
        // return the object we want as an Optional (using Optional.of(our object) )
        // (just think of an Optional as a wrapper-class for any object)
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testMusicianWithId));
        when(this.modelMapper.map(this.testMusicianWithId, MusicianDTO.class)).thenReturn(this.musicianDTOWithId);
        assertThat(this.musicianDTOWithId).isEqualTo(this.service.read(this.id));
        verify(this.repo, atLeastOnce()).findById(this.id);
    }

    @Test
    void readOneWrongIdTest() throws Exception {
        when(this.repo.findById(this.id)).thenThrow(new EntityNotFoundException());
        when(this.modelMapper.map(this.testMusicianWithId, MusicianDTO.class)).thenReturn(this.musicianDTOWithId);
        assertThrows(EntityNotFoundException.class, () -> this.service.read(this.id));
    }

    @Test
    void readAllTest() throws Exception {
        // findAll() returns a list, which is handy, since we've got one spun up :D
        when(this.repo.findAll()).thenReturn(this.musicianList);
        when(this.modelMapper.map(this.testMusicianWithId, MusicianDTO.class)).thenReturn(this.musicianDTOWithId);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() throws Exception {
        // finById() grabs a specific guitarist out of the repo
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testMusician));

        // we then save() an updated guitarist back to the repo
        // we'd normally save() it once we've done stuff to it, but instead we're just
        // feeding in an <expected>
        // that we set up at the top of this method ^
        when(this.repo.save(this.testMusician)).thenReturn(this.testMusicianWithId);
        when(this.modelMapper.map(this.testMusicianWithId, MusicianDTO.class)).thenReturn(this.musicianDTOWithId);
        assertThat(this.musicianDTOWithId).isEqualTo(this.service.update(this.musicianDTO, this.id));

        // since we've ran two when().thenReturn() methods, run a verify() on each:
        verify(this.repo, atLeastOnce()).findById(this.id);
        verify(this.repo, atLeastOnce()).save(this.testMusician);
    }

    @Test
    void deleteTest() throws Exception {
        when(this.repo.existsById(this.id)).thenReturn(true, false);
        assertThat(this.service.delete(this.id)).isTrue();
        verify(this.repo, atLeastOnce()).deleteById(this.id);
        verify(this.repo, atLeastOnce()).existsById(this.id);
    }

    @Test
    void deleteWrongIdTest() throws Exception {
        when(this.repo.existsById(this.id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> this.service.delete(this.id));
        verify(this.repo, atLeastOnce()).existsById(this.id);
    }

}