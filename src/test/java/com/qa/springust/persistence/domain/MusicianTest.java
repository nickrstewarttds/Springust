package com.qa.springust.persistence.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.global.BAND;
import com.qa.springust.global.MUSICIAN;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class MusicianTest {

    private static final Long TEST_ID = 1L;

    private Musician musician;
    private static final Musician COMPARATOR = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(),
            MUSICIAN.GUITARIST.getStrings(), MUSICIAN.GUITARIST.getType());
    private static final Musician NULL_COMPARATOR = new Musician(null, null, null);

    @BeforeEach
    void init() {
        this.musician = new Musician(TEST_ID, MUSICIAN.GUITARIST.getName(), MUSICIAN.GUITARIST.getStrings(),
                MUSICIAN.GUITARIST.getType());
    }

    @Test
    void gettersTest() {
        assertThat(this.musician.getName()).isEqualTo(COMPARATOR.getName());
        assertThat(this.musician.getStrings()).isEqualTo(COMPARATOR.getStrings());
        assertThat(this.musician.getType()).isEqualTo(COMPARATOR.getType());
    }

    @Test
    void settersTest() {
        assertThat(this.musician.getId()).isNotNull();
        assertThat(this.musician.getName()).isNotNull();
        assertThat(this.musician.getStrings()).isNotNull();
        assertThat(this.musician.getType()).isNotNull();

        this.musician.setId(null);
        this.musician.setName(null);
        this.musician.setStrings(null);
        this.musician.setType(null);
        assertThat(this.musician.getId()).isNull();
        assertThat(this.musician.getName()).isNull();
        assertThat(this.musician.getStrings()).isNull();
        assertThat(this.musician.getType()).isNull();

        assertThat(this.musician.getBand()).isNull();
        this.musician.setBand(new Band(BAND.TMG.getName()));
        assertThat(this.musician.getBand()).isNotNull();
    }

    @Test
    void equalsSameFieldsTest() {
        assertThat(this.musician).isEqualToComparingFieldByField(COMPARATOR);
    }

    @Test
    void equalsInstanceTest() {
        assertThat(this.musician).isInstanceOf(Musician.class).isNotInstanceOf(Band.class);
    }

    @Test
    void equalsObjectTest() {
        assertThat(this.musician).isEqualTo(COMPARATOR).isNotEqualTo(NULL_COMPARATOR);
    }

    @Test
    void equalsNullTest() {
        assertThat(this.musician).isNotNull();
    }

    @Test
    void equalsWithDifferentObjectTest() {
        assertThat(this.musician).isNotEqualTo(new Object());
    }

    @Test
    void checkAssignedIdTest() {
        assertThat(this.musician.getId()).isEqualTo(TEST_ID);
    }

    @Test
    void checkNullIdTest() {
        this.musician.setId(null);
        assertThat(this.musician.getId()).isNull();
    }

    @Test
    void toStringTest() {
        assertThat(this.musician).hasToString(COMPARATOR.toString());
    }

    @Test
    void hashCodeTest() {
        assertThat(this.musician).hasSameHashCodeAs(COMPARATOR);
    }

    @Test
    void hashCodeDifferentObjectTest() {
        assertThat(this.musician.hashCode()).isNotEqualTo(NULL_COMPARATOR.hashCode());
    }

}
