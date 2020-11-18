package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.qa.springust.exception.MusicianNotFoundException;
import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.persistence.repository.MusicianRepository;
import com.qa.springust.rest.dto.MusicianDTO;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class MusicianServiceUnitTest {

    @Autowired
    private MusicianService service;

    @MockBean
    private MusicianRepository repo;

    @Autowired
    private ModelMapper mapper;

    private MusicianDTO map(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Musician TEST_MUSICIAN = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
            MUSICIAN.GUITARIST.getType());

    @Test
    void createTest() throws Exception {
        TEST_MUSICIAN.setId(TEST_ID);
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        when(this.repo.save(TEST_MUSICIAN)).thenReturn(TEST_MUSICIAN);
        assertThat(this.service.create(TEST_MUSICIAN)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).save(TEST_MUSICIAN);
    }

    @Test
    void readOneTest() throws Exception {
        TEST_MUSICIAN.setId(TEST_ID);
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        when(this.repo.findById(TEST_ID)).thenReturn(Optional.of(TEST_MUSICIAN));
        assertThat(this.service.read(TEST_ID)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).findById(TEST_ID);
    }

    @Test
    void readOneWrongIdTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_MUSICIAN.setId(TEST_ID + 1);
        musicians.add(TEST_MUSICIAN);

        when(this.repo.findById(TEST_ID)).thenThrow(new MusicianNotFoundException());
        assertThrows(MusicianNotFoundException.class, () -> this.service.read(TEST_ID));
    }

    @Test
    void readAllTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_MUSICIAN.setId(TEST_ID);
        musicians.add(TEST_MUSICIAN);

        when(this.repo.findAll()).thenReturn(musicians);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() throws Exception {
        TEST_MUSICIAN.setId(TEST_ID);
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        when(this.repo.findById(TEST_ID)).thenReturn(Optional.of(TEST_MUSICIAN));
        when(this.repo.save(TEST_MUSICIAN)).thenReturn(TEST_MUSICIAN);
        assertThat(this.service.update(expected, TEST_ID)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).findById(TEST_ID);
        verify(this.repo, atLeastOnce()).save(TEST_MUSICIAN);
    }

    @Test
    void deleteTest() throws Exception {
        boolean found = false;

        when(this.repo.existsById(TEST_ID)).thenReturn(!found, found);
        assertThat(this.service.delete(TEST_ID)).isNotEqualTo(found);
        verify(this.repo, atLeastOnce()).deleteById(TEST_ID);
        verify(this.repo, atLeastOnce()).existsById(TEST_ID);
    }

    @Test
    void deleteWrongIdTest() throws Exception {
        boolean found = false;

        when(this.repo.existsById(TEST_ID)).thenReturn(found);
        assertThrows(MusicianNotFoundException.class, () -> this.service.delete(TEST_ID));
        verify(this.repo, atLeastOnce()).existsById(TEST_ID);
    }

}