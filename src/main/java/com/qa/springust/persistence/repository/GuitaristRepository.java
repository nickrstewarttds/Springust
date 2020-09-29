package com.qa.springust.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.springust.persistence.domain.Guitarist;

@Repository
public interface GuitaristRepository extends JpaRepository<Guitarist, Long> {

    // J - Java
    // P - Persistence
    // A < A - Application
    // P - Programming
    // I - Interface

    List<Guitarist> findByName(String name);

    List<Guitarist> findByNoOfStrings(Integer noOfStrings);

    List<Guitarist> findByType(String type);

}
