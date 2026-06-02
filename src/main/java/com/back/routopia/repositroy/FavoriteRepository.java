package com.back.routopia.repositroy;

import com.back.routopia.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndDestinoId(Long userId, Long destinoId);

    Optional<Favorite> findByUserIdAndDestinoId(Long userId, Long destinoId);

    void deleteByUserIdAndDestinoId(Long userId, Long destinoId);

    @Query("SELECT f.destino.id FROM Favorite f WHERE f.user.id = :userId ORDER BY f.createdAt DESC")
    List<Long> findDestinoIdsByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"destino", "destino.category", "destino.languages", "destino.traits"})
    Page<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
