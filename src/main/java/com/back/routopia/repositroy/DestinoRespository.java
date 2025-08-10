package com.back.routopia.repositroy;

import com.back.routopia.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DestinoRespository extends JpaRepository<Destino, Long>, JpaSpecificationExecutor<Destino> {
    boolean existsByNameIgnoreCase(String name);
}
