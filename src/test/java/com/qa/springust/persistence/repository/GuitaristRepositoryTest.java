package com.qa.springust.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.qa.springust.persistence.domain.Guitarist;

@DataJpaTest
class GuitaristRepositoryTest {

    // you'll only need to test your repository if you've written custom repository
    // methods
    // though i'm not using them here, i'll show you how to test three hidden
    // methods as proof-of-concept

    @Autowired
    private GuitaristRepository repo;

    private final String TEST_NAME = "The Mountain Goats";
    private final Integer TEST_STRINGS = 6;
    private final String TEST_TYPE = "Fender Talman";

    private final Guitarist TEST_GUITARIST = new Guitarist(TEST_NAME, TEST_STRINGS, TEST_TYPE);

    private Guitarist testSavedGuitarist;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testSavedGuitarist = this.repo.save(this.TEST_GUITARIST);
    }

    @Test
    void testFindByNameJPQL() {
        assertThat(this.repo.findByNameJPQL(this.testSavedGuitarist.getName())).isEqualTo(TEST_NAME);
    }

    @Test
    void testFindByStringsJPQL() {
        assertThat(this.repo.findByStringsJPQL(this.testSavedGuitarist.getStrings())).isEqualTo(TEST_STRINGS);
    }

    @Test
    void testFindByTypeJPQL() {
        assertThat(this.repo.findByTypeJPQL(this.testSavedGuitarist.getType())).isEqualTo(TEST_TYPE);
    }
}
