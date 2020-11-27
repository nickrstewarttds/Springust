package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private final Musician TEST_GUITARIST = new Musician(1L, "John Darnielle", 6, "guitarist");
    private final Musician TEST_SAXOPHONIST = new Musician(2L, "Matt Douglas", 0, "saxophonist");
    private final Musician TEST_BASSIST = new Musician(3L, "Peter Hughes", 4, "bassist");
    private final Musician TEST_DRUMMER = new Musician(4L, "Jon Wurster", 0, "drummer");

    private final List<Musician> MUSICIANS = List.of(TEST_GUITARIST, TEST_SAXOPHONIST, TEST_BASSIST, TEST_DRUMMER);

    @Test
    void createTest() throws Exception {
        when(this.repo.save(TEST_GUITARIST)).thenReturn(TEST_GUITARIST);
        assertThat(this.service.create(TEST_GUITARIST)).isEqualTo(this.mapToDTO(TEST_GUITARIST));
        verify(this.repo, atLeastOnce()).save(TEST_GUITARIST);
    }

    @Test
    void readOneTest() throws Exception {
        when(this.repo.findById(TEST_GUITARIST.getId())).thenReturn(Optional.of(TEST_GUITARIST));
        assertThat(this.service.read(TEST_GUITARIST.getId())).isEqualTo(this.mapToDTO(TEST_GUITARIST));
        verify(this.repo, atLeastOnce()).findById(TEST_GUITARIST.getId());
    }

    @Test
    void readOneWrongIdTest() throws Exception {
        when(this.repo.findById(TEST_GUITARIST.getId())).thenThrow(new MusicianNotFoundException());
        assertThrows(MusicianNotFoundException.class, () -> this.service.read(TEST_GUITARIST.getId()));
    }

    @Test
    void readAllTest() throws Exception {
        when(this.repo.findAll()).thenReturn(MUSICIANS);
        assertThat(this.service.read().isEmpty()).isFalse();
        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() throws Exception {
        when(this.repo.findById(TEST_GUITARIST.getId())).thenReturn(Optional.of(TEST_GUITARIST));
        when(this.repo.save(TEST_GUITARIST)).thenReturn(TEST_GUITARIST);
        assertThat(this.service.update(this.mapToDTO(TEST_GUITARIST), TEST_GUITARIST.getId()))
                .isEqualTo(this.mapToDTO(TEST_GUITARIST));
        verify(this.repo, atLeastOnce()).findById(TEST_GUITARIST.getId());
        verify(this.repo, atLeastOnce()).save(TEST_GUITARIST);
    }

    @Test
    void deleteTest() throws Exception {
        boolean found = false;
        when(this.repo.existsById(TEST_GUITARIST.getId())).thenReturn(found);
        assertThat(this.service.delete(TEST_GUITARIST.getId())).isNotEqualTo(found);
        verify(this.repo, atLeastOnce()).existsById(TEST_GUITARIST.getId());
    }

    @Test
    void findByNameTest() throws Exception {
        when(this.repo.findByName(TEST_GUITARIST.getName())).thenReturn(MUSICIANS);
        assertThat(this.repo.findByName(TEST_GUITARIST.getName())).asList().isEqualTo(MUSICIANS);
        verify(this.repo, atLeastOnce()).findByName(TEST_GUITARIST.getName());
    }

    @Test
    void findByStringsTest() throws Exception {
        when(this.repo.findByStrings(TEST_GUITARIST.getStrings())).thenReturn(MUSICIANS);
        assertThat(this.repo.findByStrings(TEST_GUITARIST.getStrings())).asList().isEqualTo(MUSICIANS);
        verify(this.repo, atLeastOnce()).findByStrings(TEST_GUITARIST.getStrings());
    }

    @Test
    void findByTypeTest() throws Exception {
        when(this.repo.findByType(TEST_GUITARIST.getType())).thenReturn(MUSICIANS);
        assertThat(this.repo.findByType(TEST_GUITARIST.getType())).asList().isEqualTo(MUSICIANS);
        verify(this.repo, atLeastOnce()).findByType(TEST_GUITARIST.getType());
    }

    @Test
    void orderByNameTest() throws Exception {
        when(this.repo.orderByName()).thenReturn(MUSICIANS);
        assertThat(this.repo.orderByName().stream().sorted(Comparator.comparing(Musician::getName))
                .collect(Collectors.toList()))
                        .isEqualTo(MUSICIANS.stream().sorted(Comparator.comparing(Musician::getName))
                                .collect(Collectors.toList()));
        verify(this.repo, atLeastOnce()).orderByName();
    }

    @Test
    void orderByStringsTest() throws Exception {
        when(this.repo.orderByStrings()).thenReturn(MUSICIANS);
        assertThat(this.repo.orderByStrings().stream().sorted(Comparator.comparing(Musician::getStrings))
                .collect(Collectors.toList()))
                        .isEqualTo(MUSICIANS.stream().sorted(Comparator.comparing(Musician::getStrings))
                                .collect(Collectors.toList()));
        verify(this.repo, atLeastOnce()).orderByStrings();
    }

    @Test
    void orderByTypeTest() throws Exception {
        when(this.repo.orderByType()).thenReturn(MUSICIANS);
        assertThat(this.repo.orderByType().stream().sorted(Comparator.comparing(Musician::getType))
                .collect(Collectors.toList()))
                        .isEqualTo(MUSICIANS.stream().sorted(Comparator.comparing(Musician::getType))
                                .collect(Collectors.toList()));
        verify(this.repo, atLeastOnce()).orderByType();
    }

}