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

import com.qa.springust.global.BAND;
import com.qa.springust.persistence.domain.Band;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class BandRepositoryTest {

    @Autowired
    private BandRepository repo;

    private final Band TEST_BAND1 = new Band(BAND.TMG.getName());
    private final Band TEST_BAND2 = new Band(BAND.TEG.getName());
    private final Band TEST_BAND3 = new Band(BAND.TEL.getName());

    private final List<Band> LIST_EXPECTED = new ArrayList<>();
    private List<Band> listActual;

    @BeforeAll
    void setup() {
        this.LIST_EXPECTED.add(this.TEST_BAND1);
        this.LIST_EXPECTED.add(this.TEST_BAND2);
        this.LIST_EXPECTED.add(this.TEST_BAND3);
        this.listActual = new ArrayList<>();
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.repo.saveAll(this.LIST_EXPECTED);
    }

    @Test
    void findByNameTest() throws Exception {
        this.LIST_EXPECTED.removeIf(c -> !c.getName().equals(BAND.TMG.getName()));
        this.listActual = this.repo.findByName(BAND.TMG.getName());
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getName()))
                .isEqualTo(this.listActual.stream().map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() throws Exception {
        this.LIST_EXPECTED.sort(Comparator.comparing(Band::getName));
        this.listActual = this.repo.orderByName();
        assertThat(this.LIST_EXPECTED.stream().map(e -> e.getName()))
                .isEqualTo(this.listActual.stream().map(e -> e.getName()).collect(Collectors.toList()));

    }
}
