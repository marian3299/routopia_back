package com.back.routopia.service;

import com.back.routopia.entity.Category;
import com.back.routopia.entity.Destino;
import com.back.routopia.entity.Trait;
import com.back.routopia.repositroy.DestinoRespository;
import com.back.routopia.repositroy.TraitRepository;
import com.back.routopia.specification.DestinoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestinoService {
    @Autowired
    private DestinoRespository destinoRespository;

    @Autowired
    private TraitRepository traitRepository;

    public boolean verify_name(String name) {return destinoRespository.existsByNameIgnoreCase(name);}

    /**
     * Sincroniza los traits del destino con los IDs indicados (reemplaza la asociación anterior).
     */
    public void assignTraitsToDestino(Destino destino, List<Long> traitIds) {
        if (destino.getTraits() == null) {
            destino.setTraits(new HashSet<>());
        }
        destino.getTraits().clear();
        if (traitIds == null || traitIds.isEmpty()) {
            return;
        }
        List<Long> distinctIds = traitIds.stream().distinct().collect(Collectors.toList());
        List<Trait> found = traitRepository.findAllById(distinctIds);
        if (found.size() != distinctIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uno o más IDs de características no existen");
        }
        destino.getTraits().addAll(new HashSet<>(found));
    }

    public Destino create_destino(Destino destino){
        return destinoRespository.save(destino);
    }

    public Page<Destino> list_all(Category category, String searchTerm, Pageable pageable) {
        Specification<Destino> spec = Specification.where(DestinoSpecification.hasCategory(category));

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            spec = spec.and(DestinoSpecification.searchByNameAndCity(searchTerm));
        }

        return destinoRespository.findAll(spec, pageable);
    }

    public Optional<Destino> find_by_id(Long id) { return destinoRespository.findById(id); }

    public void delete_destino(Long id) { destinoRespository.deleteById(id); }

    public Destino update_destino (Destino destino){ return destinoRespository.save(destino); }
}
