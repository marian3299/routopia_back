package com.back.routopia.controller;

import com.back.routopia.S3Service;
import com.back.routopia.dto.CategoryDTO;
import com.back.routopia.entity.Category;
import com.back.routopia.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Categories", description = "CRUD de categorías")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private S3Service s3Service;

    @Operation(summary = "Get all Categories")
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> find_all_categories(
            @RequestParam(value = "q", required = false) String searchTerm,
            @RequestParam(value = "paginate", defaultValue = "true") boolean paginate,
            @PageableDefault(size = 10) Pageable pageable) {
        if (!paginate) {
            List<Category> categories = categoryService.list_all_unpaginated(searchTerm);
            List<CategoryDTO> dtos = categories.stream()
                    .map(category -> new CategoryDTO(
                            category.getId(),
                            category.getName(),
                            category.getDescription(),
                            category.getImageUrl()))
                    .toList();
            return ResponseEntity.ok(new PageImpl<>(dtos));
        }

        Page<Category> pageResult = categoryService.list_all(searchTerm, pageable);
        Page<CategoryDTO> responsePage = pageResult.map(category -> new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl()
        ));
        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "Get Category by id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> find_category_by_id(@PathVariable Long id) {
        Category category = categoryService.find_by_id(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
        return ResponseEntity.ok(new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl()
        ));
    }

    @Operation(summary = "Create a new Category")
    @PostMapping
    public ResponseEntity<Category> create_category(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        Category savedCategory = categoryService.create_category(category);

        if (image != null && !image.isEmpty()) {
            String key = "categories/" + savedCategory.getId() + "/" + image.getOriginalFilename();
            String imageUrl = s3Service.uploadFile(image, key);
            savedCategory.setImageUrl(imageUrl);
            savedCategory = categoryService.update_category(savedCategory);
        }

        return ResponseEntity.ok(savedCategory);
    }

    @Operation(summary = "Update a Category")
    @PutMapping("/{id}")
    public ResponseEntity<Category> update_category(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        Category category = categoryService.find_by_id(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        category.setName(name);
        category.setDescription(description);

        if (image != null && !image.isEmpty()) {
            String key = "categories/" + category.getId() + "/" + image.getOriginalFilename();
            String imageUrl = s3Service.uploadFile(image, key);
            category.setImageUrl(imageUrl);
        }

        Category updatedCategory = categoryService.update_category(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "Delete a Category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete_category(@PathVariable Long id) {
        if (!categoryService.find_by_id(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }
        categoryService.delete_category(id);
        return ResponseEntity.noContent().build();
    }
}
