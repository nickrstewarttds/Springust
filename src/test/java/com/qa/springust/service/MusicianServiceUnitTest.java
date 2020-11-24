package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private MusicianDTO mapToDTO(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Musician TEST_GUITARIST = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
            MUSICIAN.GUITARIST.getType());
    private final Musician TEST_SAXOPHONIST = new Musician(TEST_ID + 1, MUSICIAN.SAXOPHONIST.getName(),
            MUSICIAN.SAXOPHONIST.getStrings(), MUSICIAN.SAXOPHONIST.getType());
    private final Musician TEST_BASSIST = new Musician(TEST_ID + 2, MUSICIAN.BASSIST.getName(),
            MUSICIAN.BASSIST.getStrings(), MUSICIAN.BASSIST.getType());
    private final Musician TEST_DRUMMER = new Musician(TEST_ID + 3, MUSICIAN.DRUMMER.getName(),
            MUSICIAN.DRUMMER.getStrings(), MUSICIAN.DRUMMER.getType());

    @Test
    void createTest() throws Exception {
        TEST_GUITARIST.setId(TEST_ID);
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);

        when(this.repo.save(TEST_GUITARIST)).thenReturn(TEST_GUITARIST);
        assertThat(this.service.create(TEST_GUITARIST)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).save(TEST_GUITARIST);
    }

    @Test
    void readOneTest() throws Exception {
        TEST_GUITARIST.setId(TEST_ID);
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);

        when(this.repo.findById(TEST_ID)).thenReturn(Optional.of(TEST_GUITARIST));
        assertThat(this.service.read(TEST_ID)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).findById(TEST_ID);
    }

    @Test
    void readOneWrongIdTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID + 1);
        musicians.add(TEST_GUITARIST);

        when(this.repo.findById(TEST_ID)).thenThrow(new MusicianNotFoundException());
        assertThrows(MusicianNotFoundException.class, () -> this.service.read(TEST_ID));
    }

    @Test
    void readAllTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID);
        musicians.add(TEST_GUITARIST);

        when(this.repo.findAll()).thenReturn(musicians);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() throws Exception {
        TEST_GUITARIST.setId(TEST_ID);
        MusicianDTO expected = this.mapToDTO(TEST_GUITARIST);

        when(this.repo.findById(TEST_ID)).thenReturn(Optional.of(TEST_GUITARIST));
        when(this.repo.save(TEST_GUITARIST)).thenReturn(TEST_GUITARIST);
        assertThat(this.service.update(expected, TEST_ID)).isEqualTo(expected);
        verify(this.repo, atLeastOnce()).findById(TEST_ID);
        verify(this.repo, atLeastOnce()).save(TEST_GUITARIST);
    }

    @Test
    void deleteTest() throws Exception {
        boolean found = false;
        when(this.repo.existsById(TEST_ID)).thenReturn(found);
        assertThat(this.service.delete(TEST_ID)).isNotEqualTo(found);
        verify(this.repo, atLeastOnce()).existsById(TEST_ID);
    }

    @Test
    void findByNameTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID);
        musicians.add(TEST_GUITARIST);

        when(this.repo.findByName(TEST_GUITARIST.getName())).thenReturn(musicians);
        assertThat(this.repo.findByName(TEST_GUITARIST.getName())).asList().isEqualTo(musicians);
        verify(this.repo, atLeastOnce()).findByName(TEST_GUITARIST.getName());
    }

    @Test
    void findByStringsTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID);
        musicians.add(TEST_GUITARIST);

        when(this.repo.findByStrings(TEST_GUITARIST.getStrings())).thenReturn(musicians);
        assertThat(this.repo.findByStrings(TEST_GUITARIST.getStrings())).asList().isEqualTo(musicians);
        verify(this.repo, atLeastOnce()).findByStrings(TEST_GUITARIST.getStrings());
    }

    @Test
    void findByTypeTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID);
        musicians.add(TEST_GUITARIST);

        when(this.repo.findByType(TEST_GUITARIST.getType())).thenReturn(musicians);
        assertThat(this.repo.findByType(TEST_GUITARIST.getType())).asList().isEqualTo(musicians);
        verify(this.repo, atLeastOnce()).findByType(TEST_GUITARIST.getType());
    }

    @Test
    void orderByNameTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID);
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_DRUMMER);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);

        when(this.repo.orderByName()).thenReturn(musicians);
        assertThat(this.repo.orderByName().stream().sorted(Comparator.comparing(Musician::getName))
                .collect(Collectors.toList())).isEqualTo(musicians);
        verify(this.repo, atLeastOnce()).orderByName();
    }

    @Test
    void orderByStringsTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID);
        musicians.add(TEST_DRUMMER);
        musicians.add(TEST_SAXOPHONIST);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_GUITARIST);

        when(this.repo.orderByStrings()).thenReturn(musicians);
        assertThat(this.repo.orderByStrings().stream().sorted(Comparator.comparing(Musician::getStrings))
                .collect(Collectors.toList())).isEqualTo(musicians);
        verify(this.repo, atLeastOnce()).orderByStrings();
    }

    @Test
    void orderByTypeTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        TEST_GUITARIST.setId(TEST_ID);
        musicians.add(TEST_BASSIST);
        musicians.add(TEST_DRUMMER);
        musicians.add(TEST_GUITARIST);
        musicians.add(TEST_SAXOPHONIST);

        when(this.repo.orderByType()).thenReturn(musicians);
        assertThat(this.repo.orderByType().stream().sorted(Comparator.comparing(Musician::getType))
                .collect(Collectors.toList())).isEqualTo(musicians);
        verify(this.repo, atLeastOnce()).orderByType();
    }

}