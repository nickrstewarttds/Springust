package com.qa.springust.rest.dto;

import static nl.jqno.equalsverifier.EqualsVerifier.simple;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MusicianDTOTest {
    @Test
    public void testEquals() throws Exception {
        simple().forClass(MusicianDTO.class).verify();
    }
}
