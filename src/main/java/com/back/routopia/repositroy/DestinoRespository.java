package com.back.routopia.repositroy;

import com.back.routopia.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DestinoRespository extends JpaRepository<Destino, Long>, JpaSpecificationExecutor<Destino> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Destino> findByNameIgnoreCase(String name);
    long countByCategoryId(Long categoryId);

    @Query("SELECT COUNT(d) FROM Destino d JOIN d.traits t WHERE t.id = :traitId")
    long countByTraitId(@Param("traitId") Long traitId);
}
