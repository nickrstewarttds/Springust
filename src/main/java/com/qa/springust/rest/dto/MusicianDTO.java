package com.qa.springust.rest.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MusicianDTO {

    // D - Data
    // T - Transfer
    // O - Object

    private Long id;
    private String name;
    private Integer strings;
    private String type;

}
