package com.qa.springust.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.persistence.domain.Guitarist;

@SpringBootTest
class GuitaristRepositoryTest {

    @Autowired
    private GuitaristRepository repo;

    private final String testNameX = "John Darnielle";
    private final Integer testStringsX = 6;
    private final String testTypeX = "Guitar";
    private final Guitarist testGuitaristX = new Guitarist(testNameX, testStringsX, testTypeX);

    private final String testNameY = "PPH";
    private final Integer testStringsY = 4;
    private final String testTypeY = "Bass";
    private final Guitarist testGuitaristY = new Guitarist(testNameY, testStringsY, testTypeY);

    private final String testNameZ = "Matty Bones";
    private final Integer testStringsZ = 12;
    private final String testTypeZ = "Sitar";
    private final Guitarist testGuitaristZ = new Guitarist(testNameZ, testStringsZ, testTypeZ);

    private final List<Guitarist> listExpected = new ArrayList<>();

    @Before
    void setup() {
        this.listExpected.add(testGuitaristX);
        this.listExpected.add(testGuitaristZ);
        this.listExpected.add(testGuitaristY);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.repo.saveAll(listExpected);
    }

    @Test
    void findByNameTest() {
        List<Guitarist> listActual = this.repo.findByName(testNameX);

        assertThat(this.listExpected.stream().map(e -> e.getName()))
                .isEqualTo(listActual.stream().map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void findByStringsTest() {
        List<Guitarist> listActual = this.repo.findByStrings(testStringsY);
        assertThat(this.listExpected.stream().map(e -> e.getStrings()))
                .isEqualTo(listActual.stream().map(e -> e.getStrings()).collect(Collectors.toList()));
    }

    @Test
    void findByTypeTest() {
        List<Guitarist> listActual = this.repo.findByType(testTypeZ);
        assertThat(this.listExpected.stream().map(e -> e.getType()))
                .isEqualTo(listActual.stream().map(e -> e.getType()).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() {
        List<Guitarist> listActual = this.repo.orderByName();
        assertThat(this.listExpected.stream().map(e -> e.getName()).equals(listActual.stream().map(e -> e.getName())));
    }

    @Test
    void orderByStringsTest() {
        List<Guitarist> listActual = this.repo.orderByStrings();
        assertThat(
                this.listExpected.stream().map(e -> e.getStrings()).equals(listActual.stream().map(e -> e.getStrings())));
    }

    @Test
    void orderByTypeTest() {
        List<Guitarist> listActual = this.repo.orderByType();
        assertThat(this.listExpected.stream().map(e -> e.getType()).equals(listActual.stream().map(e -> e.getType())));
    }

}
