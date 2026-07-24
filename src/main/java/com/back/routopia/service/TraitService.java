package com.back.routopia.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.back.routopia.repositroy.TraitRepository;
import com.back.routopia.repositroy.DestinoRespository;
import com.back.routopia.entity.Trait;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
public class TraitService {
    @Autowired
    private TraitRepository traitRepository;

    @Autowired
    private DestinoRespository destinoRepository;

    public Trait create_trait(Trait trait) {
        return traitRepository.save(trait);
    }

    public Page<Trait> list_all(String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return traitRepository.searchByName(searchTerm.trim(), pageable);
        }
        return traitRepository.findAll(pageable);
    }

    public List<Trait> list_all_unpaginated(String searchTerm) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return traitRepository.searchByNameAll(searchTerm.trim());
        }
        return traitRepository.findAll();
    }

    public Optional<Trait> find_by_id(Long id) { return traitRepository.findById(id); }

    public Trait update_trait(Trait trait) {
        return traitRepository.save(trait);
    }

    public void delete_trait(Long id) {
        long destinosCount = destinoRepository.countByTraitId(id);
        if (destinosCount > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar la característica: está asignada a " + destinosCount
                            + " destino(s). Quitala de esos destinos primero.");
        }
        traitRepository.deleteById(id);
    }
}
