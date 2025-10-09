package com.back.routopia.controller;

import com.back.routopia.S3Service;
import com.back.routopia.dto.DestinoDTO;
import com.back.routopia.entity.Category;
import com.back.routopia.entity.Destino;
import com.back.routopia.entity.Language;
import com.back.routopia.service.DestinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/destino")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Destinos", description = "CRUD de destinos")
public class DestinoController {

    @Autowired
    DestinoService destinoService;

    @Autowired
    private S3Service s3Service;


    @Operation(summary = "Get all Destinos")
    @GetMapping
    public ResponseEntity<Page<DestinoDTO>> find_all_destino(@RequestParam(required = false) Category category, @RequestParam(required = false) String q,  Pageable pageable){
        Page<Destino> pageResult = destinoService.list_all(category, q, pageable);

        Page<DestinoDTO> responsePage = pageResult.map(destino -> new DestinoDTO(
                destino.getName(),
                destino.getId(),
                destino.getCategory().name(),
                destino.getCity(),
                destino.getPrecio(),
                destino.getDuration_time(),
                destino.getDescription(),
                destino.getAddress(),
                destino.getLanguages().stream().map(Language::name).collect(Collectors.toSet()),
                destino.getPunctuation(),
                destino.getImageUrl(),
                destino.getSecondaryImages()
        ));

        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "Get Destino by id")
    @GetMapping("/{id}")
    public ResponseEntity<DestinoDTO> find_destino_by_id(@PathVariable("id") Long id) {
        Destino destino = destinoService.find_by_id(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        DestinoDTO destinoDTO = new DestinoDTO(
                destino.getName(),
                destino.getId(),
                destino.getCategory().name(),
                destino.getCity(),
                destino.getPrecio(),
                destino.getDuration_time(),
                destino.getDescription(),
                destino.getAddress(),
                destino.getLanguages().stream().map(Language::name).collect(Collectors.toSet()),
                destino.getPunctuation(),
                destino.getImageUrl(),
                destino.getSecondaryImages()
        );

        return ResponseEntity.ok(destinoDTO);
    }

    @Operation(summary = "Crear destino")
    @PostMapping
    public ResponseEntity<Destino> create_destino (
            @RequestParam("name") String name,
            @RequestParam("price") Float price,
            @RequestParam("duration") String duration,
            @RequestParam("description") String description,
            @RequestParam("languages") List<String> languages,
            @RequestParam("location") String location,
            @RequestParam("category") String category,
            @RequestParam("score") Float score,
            @RequestParam("city") String city,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "image_list", required = false) List<MultipartFile> imageList
    ){
        if (destinoService.verify_name(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del destino ya existe");
        }
        Destino destino = new Destino();
        destino.setName(name);
        destino.setPrecio(price);
        destino.setDuration_time(duration);
        destino.setDescription(description);
        destino.setAddress(location);
        destino.setCategory(Category.valueOf(category));
        destino.setPunctuation(score);
        destino.setCity(city);

        Set<Language> languageSet = languages.stream()
                .map(Language::valueOf)
                .collect(Collectors.toSet());
        destino.setLanguages(languageSet);

        Destino saved_destino = destinoService.create_destino(destino);

        if (image != null && !image.isEmpty()) {
            String key = "main-images/" + saved_destino.getId() + "/" + image.getOriginalFilename();
            String imageUrl = s3Service.uploadFile(image, key);
            saved_destino.setImageUrl(imageUrl);
        }
        if (imageList != null && !imageList.isEmpty()) {
            List<String> secondaryUrls = new ArrayList<>();

            for (MultipartFile secondaryImage : imageList) {
                if (!secondaryImage.isEmpty()) {
                    String key = "imagenes-secundarias/" + saved_destino.getId() + "/" + secondaryImage.getOriginalFilename();
                    String url = s3Service.uploadFile(secondaryImage, key);
                    secondaryUrls.add(url);
                }
            }

            // Guarda las URLs de imágenes secundarias
            saved_destino.setSecondaryImages(secondaryUrls); // Este campo debe existir
        }

        saved_destino = destinoService.create_destino(saved_destino);

        return ResponseEntity.ok(saved_destino);
    }

    @Operation(summary = "Actualizar destino")
    @PutMapping("/{id}")
    public ResponseEntity<Destino> uptade_destino(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("price") Float price,
            @RequestParam("duration") String duration,
            @RequestParam("description") String description,
            @RequestParam("languages") List<String> languages,
            @RequestParam("location") String location,
            @RequestParam("category") String category,
            @RequestParam("score") Float score,
            @RequestParam("city") String city,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "image_list", required = false) List<MultipartFile> imageList
    ){
        Optional<Destino> db_destino = destinoService.find_by_id(id);
        
        if(!db_destino.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado");
        }

        // Verificar si el nombre ya existe en otro destino (excluyendo el actual)
        if (destinoService.verify_name(name) && !db_destino.get().getName().equalsIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del destino ya existe");
        }

        Destino destino = db_destino.get();
        
        // Actualizar los campos del destino
        destino.setName(name);
        destino.setPrecio(price);
        destino.setDuration_time(duration);
        destino.setDescription(description);
        destino.setAddress(location);
        destino.setCategory(Category.valueOf(category));
        destino.setPunctuation(score);
        destino.setCity(city);

        Set<Language> languageSet = languages.stream()
                .map(Language::valueOf)
                .collect(Collectors.toSet());
        destino.setLanguages(languageSet);

        // Manejar imagen principal
        if (image != null && !image.isEmpty()) {
            String key = "main-images/" + destino.getId() + "/" + image.getOriginalFilename();
            String imageUrl = s3Service.uploadFile(image, key);
            destino.setImageUrl(imageUrl);
        }

        // Manejar imágenes secundarias
        if (imageList != null && !imageList.isEmpty()) {
            List<String> secondaryUrls = new ArrayList<>();

            for (MultipartFile secondaryImage : imageList) {
                if (!secondaryImage.isEmpty()) {
                    String key = "imagenes-secundarias/" + destino.getId() + "/" + secondaryImage.getOriginalFilename();
                    String url = s3Service.uploadFile(secondaryImage, key);
                    secondaryUrls.add(url);
                }
            }

            // Actualizar las URLs de imágenes secundarias
            destino.setSecondaryImages(secondaryUrls);
        }

        Destino updated_destino = destinoService.update_destino(destino);
        return ResponseEntity.ok(updated_destino);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete_destino(@PathVariable("id") Long id){
        Optional<Destino> db_destino = destinoService.find_by_id(id);

        if(db_destino.isPresent()){
            destinoService.delete_destino(id);
            return ResponseEntity.ok("Destino eliminado con extio");
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado");
        }
    }


}
