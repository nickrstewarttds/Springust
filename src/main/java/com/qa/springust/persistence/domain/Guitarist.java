package com.qa.springust.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
@EqualsAndHashCode
public class Guitarist {

    // organise imports with CTRL + SHIFT + O

    @Id // Primary Key
    @GeneratedValue // Auto-increment
    private Long id;

    @Column(name = "guitarist_name", unique = true)
    @Size(min = 1, max = 120) // varchar(20)
    private String name;

    @Column(name = "number_of_strings")
    @Min(4)
    @Max(12)
    private Integer noOfStrings;

    @Column(name = "guitar_type")
    @Size(min = 1, max = 120)
    private String type;

    @ManyToOne(targetEntity = Band.class)
    private Band band;

}
