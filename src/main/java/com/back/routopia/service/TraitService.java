package com.back.routopia.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.back.routopia.repositroy.TraitRepository;
import com.back.routopia.entity.Trait;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class TraitService {
    @Autowired
    private TraitRepository traitRepository;

    public Trait create_trait(Trait trait) {
        return traitRepository.save(trait);
    }

    public Page<Trait> list_all(Pageable pageable) {
        return traitRepository.findAll(pageable);
    }

    public Optional<Trait> find_by_id(Long id) { return traitRepository.findById(id); }

    public Trait update_trait(Trait trait) {
        return traitRepository.save(trait);
    }

    public void delete_trait(Long id) {
        traitRepository.deleteById(id);
    }
}
