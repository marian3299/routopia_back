package com.back.routopia.repositroy;

import com.back.routopia.entity.Trait;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraitRepository extends JpaRepository<Trait, Long> {

    @Query("SELECT t FROM Trait t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Trait> searchByName(@Param("search") String search, Pageable pageable);

    @Query("SELECT t FROM Trait t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Trait> searchByNameAll(@Param("search") String search);
}
