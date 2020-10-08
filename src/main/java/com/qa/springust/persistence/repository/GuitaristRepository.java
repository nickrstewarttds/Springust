package com.qa.springust.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.springust.persistence.domain.Guitarist;

@Repository
public interface GuitaristRepository extends JpaRepository<Guitarist, Long> {

    @Query(value = "SELECT * FROM Guitarist where name = ?1", nativeQuery = true)
    List<Guitarist> findByName(String name);

    @Query(value = "SELECT * FROM Guitarist where strings = ?1", nativeQuery = true)
    List<Guitarist> findByStrings(Integer strings);

    @Query(value = "SELECT * FROM Guitarist where type = ?1", nativeQuery = true)
    List<Guitarist> findByType(String type);

    @Query(value = "SELECT * FROM Guitarist ORDER BY name", nativeQuery = true)
    List<Guitarist> orderByName();

    @Query(value = "SELECT * FROM Guitarist ORDER BY strings", nativeQuery = true)
    List<Guitarist> orderByStrings();

    @Query(value = "SELECT * FROM Guitarist ORDER BY type", nativeQuery = true)
    List<Guitarist> orderByType();

}
