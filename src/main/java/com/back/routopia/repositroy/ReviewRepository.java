package com.back.routopia.repositroy;

import com.back.routopia.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByDestinoIdOrderByCreatedAtDesc(Long destinoId);

    boolean existsByUserIdAndDestinoId(Long userId, Long destinoId);

    Optional<Review> findByUserIdAndDestinoId(Long userId, Long destinoId);

    long countByDestinoId(Long destinoId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.destino.id = :destinoId")
    Double averageRatingByDestinoId(@Param("destinoId") Long destinoId);
}
