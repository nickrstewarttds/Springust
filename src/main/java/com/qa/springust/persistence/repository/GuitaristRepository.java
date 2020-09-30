package com.qa.springust.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.springust.persistence.domain.Guitarist;

@Repository
public interface GuitaristRepository extends JpaRepository<Guitarist, Long> {

    // J - Java
    // P - Persistence
    // A - Application Programming Interface

    @Query("SELECT p FROM Guitarist p WHERE p.name = ?1")
    Optional<Guitarist> findByNameJPQL(String name);

    @Query("SELECT p FROM Guitarist p WHERE p.strings = ?1")
    Optional<Guitarist> findByStringsJPQL(Integer strings);

    @Query("SELECT p FROM Guitarist p WHERE p.type = ?1")
    Optional<Guitarist> findByTypeJPQL(String type);

}
