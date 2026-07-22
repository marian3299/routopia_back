package com.back.routopia.controller;

import com.back.routopia.dto.ReviewRequestDTO;
import com.back.routopia.dto.ReviewSummaryDTO;
import com.back.routopia.entity.User;
import com.back.routopia.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Reviews", description = "Valoraciones y reseñas de destinos")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Listar valoraciones de un destino")
    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<ReviewSummaryDTO> getReviews(
            @PathVariable Long destinoId,
            Authentication authentication
    ) {
        User currentUser = resolveUser(authentication);
        return ResponseEntity.ok(reviewService.getReviewsForDestino(destinoId, currentUser));
    }

    @Operation(summary = "Crear una valoración (requiere reserva finalizada)")
    @PostMapping
    public ResponseEntity<ReviewSummaryDTO> createReview(
            Authentication authentication,
            @RequestBody ReviewRequestDTO request
    ) {
        User user = requireUser(authentication);
        ReviewSummaryDTO summary = reviewService.createReview(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(summary);
    }

    private User resolveUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

    private User requireUser(Authentication authentication) {
        User user = resolveUser(authentication);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return user;
    }
}
