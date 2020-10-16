package com.qa.springust.rest.dto;

import static nl.jqno.equalsverifier.EqualsVerifier.simple;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MusicianDTOTest {
    @Test
    void testEquals() throws Exception {
        simple().forClass(MusicianDTO.class).verify();
    }
}
