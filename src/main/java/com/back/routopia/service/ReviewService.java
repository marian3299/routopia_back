package com.back.routopia.service;

import com.back.routopia.dto.ReviewDTO;
import com.back.routopia.dto.ReviewRequestDTO;
import com.back.routopia.dto.ReviewSummaryDTO;
import com.back.routopia.entity.Destino;
import com.back.routopia.entity.Review;
import com.back.routopia.entity.User;
import com.back.routopia.repositroy.BookingRepository;
import com.back.routopia.repositroy.DestinoRespository;
import com.back.routopia.repositroy.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DestinoRespository destinoRespository;

    @Autowired
    private BookingRepository bookingRepository;

    public ReviewSummaryDTO getReviewsForDestino(Long destinoId, User currentUser) {
        Destino destino = destinoRespository.findById(Objects.requireNonNull(destinoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        List<ReviewDTO> reviews = reviewRepository
                .findAllByDestinoIdOrderByCreatedAtDesc(destinoId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        long total = reviewRepository.countByDestinoId(destinoId);
        Float average = resolveAverage(destinoId, destino.getPunctuation(), total);

        boolean alreadyReviewed = false;
        boolean canReview = false;

        if (currentUser != null) {
            alreadyReviewed = reviewRepository.existsByUserIdAndDestinoId(currentUser.getId(), destinoId);
            canReview = !alreadyReviewed && hasFinishedBooking(currentUser.getId(), destinoId);
        }

        return new ReviewSummaryDTO(destinoId, average, total, canReview, alreadyReviewed, reviews);
    }

    @Transactional
    public ReviewSummaryDTO createReview(User user, ReviewRequestDTO request) {
        if (request.getDestinoId() == null || request.getRating() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "destinoId y rating son obligatorios");
        }
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La puntuación debe ser entre 1 y 5");
        }

        Long destinoId = request.getDestinoId();
        Destino destino = destinoRespository.findById(Objects.requireNonNull(destinoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        if (reviewRepository.existsByUserIdAndDestinoId(user.getId(), destinoId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya valoraste este producto");
        }

        if (!hasFinishedBooking(user.getId(), destinoId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo podés valorar productos con una reserva finalizada"
            );
        }

        Review review = new Review();
        review.setUser(user);
        review.setDestino(destino);
        review.setRating(request.getRating());
        review.setComment(
                request.getComment() != null && !request.getComment().isBlank()
                        ? request.getComment().trim()
                        : null
        );
        reviewRepository.save(review);

        refreshDestinoAverage(destino);

        return getReviewsForDestino(destinoId, user);
    }

    public long countByDestinoId(Long destinoId) {
        return reviewRepository.countByDestinoId(destinoId);
    }

    private boolean hasFinishedBooking(Long userId, Long destinoId) {
        return bookingRepository.existsByUserIdAndDestinoIdAndStatusAndBookingDateLessThanEqual(
                userId,
                destinoId,
                "CONFIRMED",
                LocalDate.now()
        );
    }

    private void refreshDestinoAverage(Destino destino) {
        Double avg = reviewRepository.averageRatingByDestinoId(destino.getId());
        if (avg == null) {
            return;
        }
        float rounded = Math.round(avg * 10f) / 10f;
        destino.setPunctuation(rounded);
        destinoRespository.save(destino);
    }

    private Float resolveAverage(Long destinoId, Float fallback, long total) {
        if (total <= 0) {
            return fallback != null ? fallback : 0f;
        }
        Double avg = reviewRepository.averageRatingByDestinoId(destinoId);
        if (avg == null) {
            return fallback != null ? fallback : 0f;
        }
        return Math.round(avg * 10f) / 10f;
    }

    private ReviewDTO toDto(Review review) {
        String fullName = ((review.getUser().getNombre() != null ? review.getUser().getNombre() : "")
                + " "
                + (review.getUser().getApellido() != null ? review.getUser().getApellido() : "")).trim();
        if (fullName.isBlank()) {
            fullName = review.getUser().getEmail();
        }

        return new ReviewDTO(
                review.getId(),
                review.getDestino().getId(),
                review.getUser().getId(),
                fullName,
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
