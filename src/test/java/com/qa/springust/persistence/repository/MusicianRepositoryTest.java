package com.qa.springust.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.global.MUSICIAN;
import com.qa.springust.persistence.domain.Musician;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class MusicianRepositoryTest {

    @Autowired
    private MusicianRepository repo;

    private final Musician TEST_GUITARIST = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
            MUSICIAN.GUITARIST.getType());
    private final Musician TEST_SAXOPHONIST = new Musician(MUSICIAN.SAXOPHONIST.getName(),
            MUSICIAN.SAXOPHONIST.getStrings(), MUSICIAN.SAXOPHONIST.getType());
    private final Musician TEST_BASSIST = new Musician(MUSICIAN.BASSIST.getName(), MUSICIAN.BASSIST.getStrings(),
            MUSICIAN.BASSIST.getType());
    private final Musician TEST_DRUMMER = new Musician(MUSICIAN.DRUMMER.getName(), MUSICIAN.DRUMMER.getStrings(),
            MUSICIAN.DRUMMER.getType());

    private final List<Musician> DATA_SET = List.of(TEST_GUITARIST, TEST_SAXOPHONIST, TEST_BASSIST, TEST_DRUMMER);

    @BeforeAll
    void setup() {
        this.repo.saveAll(DATA_SET);
    }

    @Test
    void findByNameTest() throws Exception {
        assertThat(this.repo.findByName(MUSICIAN.GUITARIST.getName()).stream().map(e -> e.getName())
                .collect(Collectors.toList()))
                        .isEqualTo(DATA_SET.stream().filter(e -> e.getName().equals(TEST_GUITARIST.getName()))
                                .map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void findByStringsTest() throws Exception {
        assertThat(this.repo.findByStrings(MUSICIAN.GUITARIST.getStrings()).stream().map(e -> e.getStrings())
                .collect(Collectors.toList()))
                        .isEqualTo(DATA_SET.stream().filter(e -> e.getStrings().equals(TEST_GUITARIST.getStrings()))
                                .map(e -> e.getStrings()).collect(Collectors.toList()));
    }

    @Test
    void findByTypeTest() throws Exception {
        assertThat(this.repo.findByType(MUSICIAN.GUITARIST.getType()).stream().map(e -> e.getType())
                .collect(Collectors.toList()))
                        .isEqualTo(DATA_SET.stream().filter(e -> e.getType().equals(TEST_GUITARIST.getType()))
                                .map(e -> e.getType()).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() throws Exception {
        assertThat(this.repo.orderByName().stream().map(e -> e.getName()).collect(Collectors.toList()))
                .isEqualTo(DATA_SET.stream().sorted(Comparator.comparing(Musician::getName)).map(e -> e.getName())
                        .collect(Collectors.toList()));
    }

    @Test
    void orderByStringsTest() throws Exception {
        assertThat(this.repo.orderByStrings().stream().map(e -> e.getStrings()).collect(Collectors.toList()))
                .isEqualTo(DATA_SET.stream().sorted(Comparator.comparing(Musician::getStrings)).map(e -> e.getStrings())
                        .collect(Collectors.toList()));
    }

    @Test
    void orderByTypeTest() throws Exception {
        assertThat(this.repo.orderByType().stream().map(e -> e.getType()).collect(Collectors.toList()))
                .isEqualTo(DATA_SET.stream().sorted(Comparator.comparing(Musician::getType)).map(e -> e.getType())
                        .collect(Collectors.toList()));
    }

}
