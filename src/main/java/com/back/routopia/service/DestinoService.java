package com.back.routopia.service;

import com.back.routopia.entity.Category;
import com.back.routopia.entity.Destino;
import com.back.routopia.repositroy.DestinoRespository;
import com.back.routopia.specification.DestinoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class DestinoService {
    @Autowired
    private DestinoRespository destinoRespository;

    public boolean verify_name(String name) {return destinoRespository.existsByNameIgnoreCase(name);}

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
