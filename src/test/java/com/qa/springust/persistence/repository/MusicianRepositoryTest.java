package com.qa.springust.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    private final Musician testGuitarist = new Musician(MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
            MUSICIAN.GUITARIST.getType());
    private final Musician testBassist = new Musician(MUSICIAN.BASSIST.getName(), MUSICIAN.BASSIST.getStrings(),
            MUSICIAN.BASSIST.getType());
    private final Musician testSaxophonist = new Musician(MUSICIAN.SAXOPHONIST.getName(),
            MUSICIAN.SAXOPHONIST.getStrings(), MUSICIAN.SAXOPHONIST.getType());
    private final Musician testDrummer = new Musician(MUSICIAN.DRUMMER.getName(), MUSICIAN.DRUMMER.getStrings(),
            MUSICIAN.DRUMMER.getType());

    private final List<Musician> LIST_EXPECTED = new ArrayList<>();
    private List<Musician> listActual;

    @BeforeAll
    void setup() {
        this.LIST_EXPECTED.add(this.testGuitarist);
        this.LIST_EXPECTED.add(this.testSaxophonist);
        this.LIST_EXPECTED.add(this.testBassist);
        this.LIST_EXPECTED.add(this.testDrummer);
        this.listActual = new ArrayList<>();
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.repo.saveAll(this.LIST_EXPECTED);
    }

    @Test
    void findByNameTest() throws Exception {
        this.LIST_EXPECTED.removeIf(c -> !c.getName().equals(MUSICIAN.GUITARIST.getName()));
        this.listActual = this.repo.findByName(MUSICIAN.GUITARIST.getName());
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getName()))
                .isEqualTo(this.listActual.stream().map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void findByStringsTest() throws Exception {
        this.LIST_EXPECTED.removeIf(c -> !c.getStrings().equals(MUSICIAN.BASSIST.getStrings()));
        this.listActual = this.repo.findByStrings(MUSICIAN.BASSIST.getStrings());
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getStrings()))
                .isEqualTo(this.listActual.stream().map(e -> e.getStrings()).collect(Collectors.toList()));
    }

    @Test
    void findByTypeTest() throws Exception {
        this.LIST_EXPECTED.removeIf(c -> !c.getType().equals(MUSICIAN.SAXOPHONIST.getType()));
        this.listActual = this.repo.findByType(MUSICIAN.SAXOPHONIST.getType());
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getType()))
                .isEqualTo(this.listActual.stream().map(e -> e.getType()).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() throws Exception {
        this.LIST_EXPECTED.sort(Comparator.comparing(Musician::getName));
        this.listActual = this.repo.orderByName();
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getName()))
                .isEqualTo(this.listActual.stream().map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void orderByStringsTest() throws Exception {
        this.LIST_EXPECTED.sort(Comparator.comparing(Musician::getStrings));
        this.listActual = this.repo.orderByStrings();
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getStrings()))
                .isEqualTo(this.listActual.stream().map(e -> e.getStrings()).collect(Collectors.toList()));
    }

    @Test
    void orderByTypeTest() throws Exception {
        this.LIST_EXPECTED.sort(Comparator.comparing(Musician::getType));
        this.listActual = this.repo.orderByType();
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getType()))
                .isEqualTo(this.listActual.stream().map(e -> e.getType()).collect(Collectors.toList()));
    }

}
