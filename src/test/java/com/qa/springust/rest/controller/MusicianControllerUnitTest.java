package com.qa.springust.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
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
    private ModelMapper mapper;

    // and we need the dto mapping as well, otherwise we can't test
    // our controller methods (which rely on RE<xDTO>)
    private MusicianDTO map(Musician musician) {
        return this.mapper.map(musician, MusicianDTO.class);
    }

    private final Long TEST_ID = 1L;

    private final Musician TEST_MUSICIAN = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());

    @Test
    void createTest() throws Exception {
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        when(this.service.create(TEST_MUSICIAN)).thenReturn(expected);
        assertThat(new ResponseEntity<MusicianDTO>(expected, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(TEST_MUSICIAN));
        verify(this.service, times(1)).create(TEST_MUSICIAN);
    }

    @Test
    void readOneTest() throws Exception {
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        when(this.service.read(TEST_ID)).thenReturn(expected);
        assertThat(new ResponseEntity<MusicianDTO>(expected, HttpStatus.OK)).isEqualTo(this.controller.read(TEST_ID));
        verify(this.service, times(1)).read(TEST_ID);
    }

    @Test
    void readAllTest() throws Exception {
        List<Musician> musicians = new ArrayList<>();
        musicians.add(TEST_MUSICIAN);

        when(this.service.read()).thenReturn(musicians.stream().map(this::map).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, times(1)).read();
    }

    @Test
    void updateTest() throws Exception {
        MusicianDTO expected = this.map(TEST_MUSICIAN);

        when(this.service.update(expected, TEST_ID)).thenReturn(expected);
        assertThat(new ResponseEntity<MusicianDTO>(expected, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(TEST_ID, expected));
        verify(this.service, times(1)).update(expected, TEST_ID);
    }

    @Test
    void deleteTest() throws Exception {
        this.controller.delete(TEST_ID);
        verify(this.service, times(1)).delete(TEST_ID);
    }

}