package com.qa.springust.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.springust.persistence.domain.Guitarist;

@Repository
public interface GuitaristRepository extends JpaRepository<Guitarist, Long> {

    // J - Java
    // P - Persistence
    // A - Application Programming Interface

//    List<Guitarist> findByName(String name);
//
//    List<Guitarist> findByStrings(Integer strings);
//
//    List<Guitarist> findByType(String type);

}
