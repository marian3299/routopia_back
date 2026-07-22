package com.back.routopia.dto;

import java.util.List;

public class ReviewSummaryDTO {
    private Long destinoId;
    private Float averageRating;
    private long totalReviews;
    private boolean canReview;
    private boolean alreadyReviewed;
    private List<ReviewDTO> reviews;

    public ReviewSummaryDTO(
            Long destinoId,
            Float averageRating,
            long totalReviews,
            boolean canReview,
            boolean alreadyReviewed,
            List<ReviewDTO> reviews
    ) {
        this.destinoId = destinoId;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.canReview = canReview;
        this.alreadyReviewed = alreadyReviewed;
        this.reviews = reviews;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public boolean isCanReview() {
        return canReview;
    }

    public boolean isAlreadyReviewed() {
        return alreadyReviewed;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }
}
