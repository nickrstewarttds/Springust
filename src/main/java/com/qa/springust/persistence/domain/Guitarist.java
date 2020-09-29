package com.qa.springust.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Guitarist {

    @Id // Primary Key
    @GeneratedValue
    private Long id;

    @Column(name = "guitarist_name", unique = true)
    @NotNull
    @Size(min = 0, max = 55)
    private String name;

    @Min(4)
    @Max(12)
    private Integer noOfStrings;

    @NotNull
    private String type;

    @ManyToOne(targetEntity = Band.class)
    private Band band;

    public Guitarist(String name, Integer noOfStrings, String type) {
        super();
        this.name = name;
        this.noOfStrings = noOfStrings;
        this.type = type;
    }

}
