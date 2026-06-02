package com.back.routopia.controller;

import com.back.routopia.dto.FavoriteToggleDTO;
import com.back.routopia.entity.User;
import com.back.routopia.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Favoritos", description = "Marcar y consultar destinos favoritos")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "Obtener IDs de destinos favoritos del usuario autenticado")
    @GetMapping
    public ResponseEntity<List<Long>> getFavorites(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(favoriteService.getFavoriteDestinoIds(user));
    }

    @Operation(summary = "Marcar o desmarcar un destino como favorito")
    @PostMapping("/{destinoId}/toggle")
    public ResponseEntity<FavoriteToggleDTO> toggleFavorite(
            Authentication authentication,
            @PathVariable Long destinoId
    ) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(favoriteService.toggleFavorite(user, destinoId));
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "Usuario no autenticado"
            );
        }
        return user;
    }
}
