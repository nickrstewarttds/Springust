package com.qa.springust.persistence.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.springust.global.BAND;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class BandTest {

    private Band band;
    private static final Band COMPARATOR = new Band(BAND.TMG.getName());
    private static final Band NULL_COMPARATOR = new Band(null);

    private final Long ID = 1L;

    @BeforeEach
    void init() {
        this.band = new Band(BAND.TMG.getName());
        this.band.setId(this.ID);
        COMPARATOR.setId(this.ID);
    }

    @Test
    void gettersTest() {
        assertThat(this.band.getName()).isEqualTo(COMPARATOR.getName());

    }

    @Test
    void settersTest() {
        assertThat(this.band.getId()).isNotNull();
        assertThat(this.band.getName()).isNotNull();

        this.band.setId(null);
        this.band.setName(null);
        assertThat(this.band.getId()).isNull();
        assertThat(this.band.getName()).isNull();

        assertThat(this.band.getMusicians()).isEmpty();
        this.band.setMusicians(new ArrayList<Musician>());
        assertThat(this.band.getMusicians()).isNotNull();
    }

    @Test
    void equalsSameFieldsTest() {
        assertThat(this.band).isEqualToComparingFieldByField(COMPARATOR);
    }

    @Test
    void equalsInstanceTest() {
        assertThat(this.band).isInstanceOf(Band.class).isNotInstanceOf(Musician.class);
    }

    @Test
    void equalsObjectTest() {
        assertThat(this.band).isEqualTo(COMPARATOR).isNotEqualTo(NULL_COMPARATOR);
    }

    @Test
    void equalsNullTest() {
        assertThat(this.band).isNotNull();
    }

    @Test
    void equalsWithDifferentObjectTest() {
        assertThat(this.band).isNotEqualTo(new Object());
    }

    @Test
    void checkAssignedIdTest() {
        assertThat(ID).isEqualTo(this.band.getId());
    }

    @Test
    void checkNullIdTest() {
        this.band.setId(null);
        assertThat(this.band.getId()).isNull();
    }

    @Test
    void toStringTest() {
        assertThat(this.band).hasToString(COMPARATOR.toString());
    }

    @Test
    void hashCodeTest() {
        assertThat(this.band).hasSameHashCodeAs(COMPARATOR);
    }

    @Test
    void hashCodeDifferentObjectTest() {
        assertThat(this.band.hashCode()).isNotEqualTo(NULL_COMPARATOR.hashCode());
    }

}
