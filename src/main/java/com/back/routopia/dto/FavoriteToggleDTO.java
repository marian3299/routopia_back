package com.back.routopia.dto;

public class FavoriteToggleDTO {
    private Long destinoId;
    private boolean favorite;

    public FavoriteToggleDTO(Long destinoId, boolean favorite) {
        this.destinoId = destinoId;
        this.favorite = favorite;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
