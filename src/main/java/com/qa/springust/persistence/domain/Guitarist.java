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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Guitarist {

    // organise imports with CTRL + SHIFT + O

    @Id // Primary Key
    @GeneratedValue // Auto-increment
    private long id;

    @Column(name = "guitarist_name", unique = true)
    @NotNull
    @Size(min = 1, max = 120) // varchar(20)
    private String name;

    @Column(name = "strings")
    @Min(4)
    @Max(12)
    private int noOfStrings;

    @Column(name = "type")
    @NotNull
    @Size(min = 1, max = 120)
    private String type;

    @ManyToOne(targetEntity = Band.class)
    private Band band;

    // generate code using SHIFT + ALT + S
    public Guitarist(@NotNull @Size(min = 1, max = 120) String name, @Min(4) @Max(12) int noOfStrings,
            @NotNull @Size(min = 1, max = 120) String type) {
        super();
        this.name = name;
        this.noOfStrings = noOfStrings;
        this.type = type;
    }

}
