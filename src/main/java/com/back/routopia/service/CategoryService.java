package com.back.routopia.service;

import com.back.routopia.entity.Category;
import com.back.routopia.repositroy.CategoryRepository;
import com.back.routopia.repositroy.DestinoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DestinoRespository destinoRepository;

    public Category create_category(Category category) {
        return categoryRepository.save(category);
    }

    public Page<Category> list_all(String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return categoryRepository.searchByName(searchTerm.trim(), pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    public List<Category> list_all_unpaginated(String searchTerm) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return categoryRepository.searchByNameAll(searchTerm.trim());
        }
        return categoryRepository.findAll();
    }

    public Optional<Category> find_by_id(Long id) {
        return categoryRepository.findById(id);
    }

    public Category update_category(Category category) {
        return categoryRepository.save(category);
    }

    public void delete_category(Long id) {
        long destinosCount = destinoRepository.countByCategoryId(id);
        if (destinosCount > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar la categoría: tiene " + destinosCount
                            + " destino(s) asociado(s). Reasigná o eliminá esos destinos primero.");
        }
        categoryRepository.deleteById(id);
    }
}
