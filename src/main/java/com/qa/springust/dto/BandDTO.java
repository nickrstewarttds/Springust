package com.qa.springust.dto;

import java.util.List;

import com.qa.springust.persistence.domain.Guitarist;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(lazy = true)
@Setter
@ToString
@EqualsAndHashCode
public class BandDTO {

    private Long id;
    private String name;
    private List<Guitarist> guitarists;

}
