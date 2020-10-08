package com.qa.springust.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.springust.persistence.domain.Band;

@Repository
public interface BandRepository extends JpaRepository<Band, Long> {

    @Query(value = "SELECT * FROM Band where name = ?1", nativeQuery = true)
    List<Band> findByName(String name);

    @Query(value = "SELECT * FROM Band ORDER BY name", nativeQuery = true)
    List<Band> orderByNameAZ();

}
