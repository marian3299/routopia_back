package com.back.routopia.service;

import com.back.routopia.dto.DestinoDTO;
import com.back.routopia.dto.FavoriteToggleDTO;
import com.back.routopia.entity.Destino;
import com.back.routopia.entity.Favorite;
import com.back.routopia.entity.Language;
import com.back.routopia.entity.User;
import com.back.routopia.repositroy.DestinoRespository;
import com.back.routopia.repositroy.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private DestinoRespository destinoRespository;

    public List<Long> getFavoriteDestinoIds(User user) {
        return favoriteRepository.findDestinoIdsByUserId(user.getId());
    }

    public Page<DestinoDTO> getFavoriteDestinos(User user, Pageable pageable) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(favorite -> mapDestinoToDto(favorite.getDestino()));
    }

    @Transactional
    public FavoriteToggleDTO toggleFavorite(User user, Long destinoId) {
        if (destinoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "destinoId es obligatorio");
        }

        destinoRespository.findById(Objects.requireNonNull(destinoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        if (favoriteRepository.existsByUserIdAndDestinoId(user.getId(), destinoId)) {
            favoriteRepository.deleteByUserIdAndDestinoId(user.getId(), destinoId);
            return new FavoriteToggleDTO(destinoId, false);
        }

        Destino destino = destinoRespository.findById(destinoId).orElseThrow();
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setDestino(destino);
        favoriteRepository.save(favorite);

        return new FavoriteToggleDTO(destinoId, true);
    }

    private DestinoDTO mapDestinoToDto(Destino destino) {
        return new DestinoDTO(
                destino.getName(),
                destino.getId(),
                destino.getCategory() != null ? destino.getCategory().getId() : null,
                destino.getCategory() != null ? destino.getCategory().getName() : null,
                destino.getCity(),
                destino.getPrecio(),
                destino.getDuration_time(),
                destino.getDescription(),
                destino.getAddress(),
                destino.getLanguages().stream().map(Language::name).collect(Collectors.toSet()),
                destino.getPunctuation(),
                destino.getImageUrl(),
                destino.getSecondaryImages(),
                DestinoDTO.traitsFromEntity(destino.getTraits()),
                DestinoDTO.policiesFromEntity(destino.getPolicies())
        );
    }
}
