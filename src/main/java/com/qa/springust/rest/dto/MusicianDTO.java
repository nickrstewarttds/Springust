package com.qa.springust.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MusicianDTO {

    // D - Data
    // T - Transfer
    // O - Object

    private Long id;
    private String name;
    private Integer strings;
    private String type;

}
