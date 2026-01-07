package com.back.routopia.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.back.routopia.entity.Trait;

@Repository
public interface TraitRepository extends JpaRepository<Trait, Long> {
    
}
