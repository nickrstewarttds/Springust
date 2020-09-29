package com.qa.springust.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// converting our POJO (Guitarist entity) to JSON (to view in the front-end)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GuitaristDTO {

    // D - Data
    // T - Transfer
    // O - Object

    private long id;
    private String name;
    private int noOfStrings;
    private String type;

}
