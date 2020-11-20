package com.qa.springust.rest.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BandDTO {

    private Long id;
    private String name;
    private List<MusicianDTO> musicians = new ArrayList<>();

}
