package com.back.routopia.service;

import com.back.routopia.entity.Category;
import com.back.routopia.repositroy.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category create_category(Category category) {
        return categoryRepository.save(category);
    }

    public Page<Category> list_all(String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return categoryRepository.searchByName(searchTerm.trim(), pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    public Optional<Category> find_by_id(Long id) {
        return categoryRepository.findById(id);
    }

    public Category update_category(Category category) {
        return categoryRepository.save(category);
    }

    public void delete_category(Long id) {
        categoryRepository.deleteById(id);
    }
}
