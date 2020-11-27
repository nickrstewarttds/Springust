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

import com.qa.springust.persistence.domain.Musician;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class MusicianRepositoryTest {

    @Autowired
    private MusicianRepository repo;

    private final Musician TEST_GUITARIST = new Musician("John Darnielle", 6, "guitarist");
    private final Musician TEST_SAXOPHONIST = new Musician("Matt Douglas", 0, "saxophonist");
    private final Musician TEST_BASSIST = new Musician("Peter Hughes", 4, "bassist");
    private final Musician TEST_DRUMMER = new Musician("Jon Wurster", 0, "drummer");

    private final List<Musician> DATA_SET = List.of(TEST_GUITARIST, TEST_SAXOPHONIST, TEST_BASSIST, TEST_DRUMMER);

    @BeforeAll
    void setup() {
        this.repo.saveAll(DATA_SET);
    }

    @Test
    void findByNameTest() throws Exception {
        assertThat(this.repo.findByName(TEST_GUITARIST.getName()).stream().map(e -> e.getName())
                .collect(Collectors.toList()))
                        .isEqualTo(DATA_SET.stream().filter(e -> e.getName().equals(TEST_GUITARIST.getName()))
                                .map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void findByStringsTest() throws Exception {
        assertThat(this.repo.findByStrings(TEST_GUITARIST.getStrings()).stream().map(e -> e.getStrings())
                .collect(Collectors.toList()))
                        .isEqualTo(DATA_SET.stream().filter(e -> e.getStrings().equals(TEST_GUITARIST.getStrings()))
                                .map(e -> e.getStrings()).collect(Collectors.toList()));
    }

    @Test
    void findByTypeTest() throws Exception {
        assertThat(this.repo.findByType(TEST_GUITARIST.getType()).stream().map(e -> e.getType())
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
