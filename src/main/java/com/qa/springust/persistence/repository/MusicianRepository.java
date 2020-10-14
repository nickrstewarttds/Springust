package com.qa.springust.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.springust.persistence.domain.Musician;

@Repository
public interface MusicianRepository extends JpaRepository<Musician, Long> {

    @Query(value = "SELECT * FROM Musician WHERE name = ?1", nativeQuery = true)
    List<Musician> findByName(String name);

    @Query(value = "SELECT * FROM Musician WHERE strings = ?1", nativeQuery = true)
    List<Musician> findByStrings(Integer strings);

    @Query(value = "SELECT * FROM Musician WHERE type = ?1", nativeQuery = true)
    List<Musician> findByType(String type);

    @Query(value = "SELECT * FROM Musician ORDER BY name", nativeQuery = true)
    List<Musician> orderByName();

    @Query(value = "SELECT * FROM Musician ORDER BY strings", nativeQuery = true)
    List<Musician> orderByStrings();

    @Query(value = "SELECT * FROM Musician ORDER BY type", nativeQuery = true)
    List<Musician> orderByType();

}
