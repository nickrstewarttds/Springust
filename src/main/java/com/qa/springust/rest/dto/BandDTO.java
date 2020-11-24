package com.qa.springust.rest.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BandDTO {

    private Long id;
    private String name;
    private List<MusicianDTO> musicians = new ArrayList<>();

}
