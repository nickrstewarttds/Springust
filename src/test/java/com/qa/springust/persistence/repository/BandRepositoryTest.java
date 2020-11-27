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

import com.qa.springust.persistence.domain.Band;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class BandRepositoryTest {

    @Autowired
    private BandRepository repo;

    private final Band TEST_BAND1 = new Band("The Mountain Goats");
    private final Band TEST_BAND2 = new Band("The Extra Glenns");
    private final Band TEST_BAND3 = new Band("The Congress");

    private final List<Band> DATA_SET = List.of(TEST_BAND1, TEST_BAND2, TEST_BAND3);

    @BeforeAll
    void init() {
        this.repo.saveAll(DATA_SET);
    }

    @Test
    void findByNameTest() throws Exception {
        assertThat(
                this.repo.findByName(TEST_BAND1.getName()).stream().map(e -> e.getName()).collect(Collectors.toList()))
                        .isEqualTo(DATA_SET.stream().filter(e -> e.getName().equals("The Mountain Goats"))
                                .map(e -> e.getName()).collect(Collectors.toList()));
    }

    @Test
    void orderByNameTest() throws Exception {
        assertThat(this.repo.orderByName().stream().map(e -> e.getName()).collect(Collectors.toList()))
                .isEqualTo(DATA_SET.stream().sorted(Comparator.comparing(Band::getName)).map(e -> e.getName())
                        .collect(Collectors.toList()));

    }
}
