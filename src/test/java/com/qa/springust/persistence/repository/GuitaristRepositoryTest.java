package com.qa.springust.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.qa.springust.persistence.domain.Guitarist;

@DataJpaTest
class GuitaristRepositoryTest {

    // you'll only need to test your repo if you've written custom methods

    @Autowired
    private GuitaristRepository repo;

    private final String TEST_NAME = "The Mountain Goats";
    private final Integer TEST_STRINGS = 6;
    private final String TEST_TYPE = "Fender Talman";

    private final Guitarist TEST_GUITARIST = new Guitarist(TEST_NAME, TEST_STRINGS, TEST_TYPE);

    private List<Guitarist> results;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.results = new ArrayList<>();
    }

    @Test
    void testFindByNameJPQL() throws Exception {
        this.results.add(TEST_GUITARIST);
        assertThat(this.repo.findByNameJPQL(TEST_GUITARIST.getName())).isEqualTo(results);
    }

    @Test
    void testFindByStringsJPQL() throws Exception {
        this.results.add(TEST_GUITARIST);
        assertThat(this.repo.findByStringsJPQL(TEST_GUITARIST.getStrings())).isEqualTo(results);
    }

    @Test
    public void testFindByTypeJPQL() throws Exception {
        this.results.add(TEST_GUITARIST);
        assertThat(this.repo.findByTypeJPQL(TEST_GUITARIST.getType())).isEqualTo(results);
    }
}
