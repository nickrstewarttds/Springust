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

import com.qa.springust.persistence.domain.Band;

@SpringBootTest
class BandRepositoryTest {

    @Autowired
    private BandRepository repo;

    private final String testNameX = "The Mountain Goats";
    private final String testNameY = "The Extra Glenns";
    private final String testNameZ = "The Seneca Twins";

    private final Band testBandX = new Band(testNameX);
    private final Band testBandY = new Band(testNameY);
    private final Band testBandZ = new Band(testNameZ);

    private final List<Band> listExpected = new ArrayList<>();

    @Before
    void setup() {
        this.listExpected.add(testBandX);
        this.listExpected.add(testBandY);
        this.listExpected.add(testBandZ);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.repo.saveAll(listExpected);
    }

    @Test
    void findByNameTest() {
        List<Band> listActual = this.repo.findByName(testNameX);

        assertThat(this.listExpected.stream().map(e -> e.getName()))
                .isEqualTo(listActual.stream().map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() {
        List<Band> listActual = this.repo.orderByName();
        assertThat(this.listExpected.stream().map(e -> e.getName()).equals(listActual.stream().map(e -> e.getName())));

    }
}
