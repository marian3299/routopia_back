package com.back.routopia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import com.back.routopia.service.TraitService;
import com.back.routopia.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.back.routopia.entity.Trait;
import com.back.routopia.dto.TraitDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/traits")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Traits", description = "CRUD de traits")
public class TraitController {

    @Autowired
    private TraitService traitService;

    @Autowired
    private S3Service s3Service;

    @Operation(summary = "Get all Traits")
    @GetMapping
    public ResponseEntity<Page<TraitDTO>> find_all_traits(Pageable pageable) {
        Page<Trait> pageResult = traitService.list_all(pageable);
        Page<TraitDTO> responsePage = pageResult.map(trait -> new TraitDTO(
            trait.getId(),
            trait.getName(),
            trait.getImageUrl()   
        ));
        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "Create a new Trait")
    @PostMapping
    public ResponseEntity<Trait> create_trait(
        @RequestParam("name") String name,
        @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        Trait trait = new Trait();
        trait.setName(name);

        Trait saved_trait = traitService.create_trait(trait);

        if (image != null && !image.isEmpty()) {
            String key = "traits/" + saved_trait.getId() + "/" + image.getOriginalFilename();
            String imageUrl = s3Service.uploadFile(image, key);
            saved_trait.setImageUrl(imageUrl);
        }

        saved_trait = traitService.create_trait(saved_trait);

        return ResponseEntity.ok(saved_trait);
    }

    @Operation(summary = "Update a Trait")
    @PutMapping("/{id}")
    public ResponseEntity<Trait> update_trait(
        @PathVariable Long id,
        @RequestParam("name") String name,
        @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        Optional<Trait> db_trait = traitService.find_by_id(id);

        if(!db_trait.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trait not found");
        }

        Trait trait = db_trait.get();
        trait.setName(name);
        if (image != null && !image.isEmpty()) {
            String key = "traits/" + trait.getId() + "/" + image.getOriginalFilename();
            String imageUrl = s3Service.uploadFile(image, key);
            trait.setImageUrl(imageUrl);
        }
        Trait updated_trait = traitService.update_trait(trait);

        return ResponseEntity.ok(updated_trait);
    }

    @Operation(summary = "Delete a Trait")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete_trait(@PathVariable Long id) {
        traitService.delete_trait(id);
        return ResponseEntity.noContent().build();
    }
}
