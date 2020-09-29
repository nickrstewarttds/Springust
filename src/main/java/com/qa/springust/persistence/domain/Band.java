package com.qa.springust.persistence.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class Band {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "band_name", unique = true)
    private String name;

    @OneToMany(targetEntity = Guitarist.class, cascade = CascadeType.ALL)
    private List<Guitarist> guitarists = new ArrayList<>();

    // constructor for making a 'blank' band
    public Band(String name) {
        super();
        this.name = name;
    }

}
