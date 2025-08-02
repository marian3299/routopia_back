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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/destino")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Destinos", description = "CRUD de destinos")
public class DestinoController {

    @Autowired
    DestinoService destinoService;

    @Autowired
    private S3Service s3Service;


    @Operation(summary = "Get all Destinos")
    @GetMapping
    public ResponseEntity<List<DestinoDTO>> find_all_destino(@RequestParam(required = false) Category category){
        List<Destino> destinos = destinoService.list_all(category);

        List<DestinoDTO> response_list = destinos.stream()
                .map(destino -> new DestinoDTO(
                        destino.getName(),
                        destino.getId(),
                        destino.getCategory().name(),
                        destino.getCity(),
                        destino.getPrecio(),
                        destino.getDuration_time(),
                        destino.getDescription(),
                        destino.getAddress(),
                        destino.getLanguages().stream()
                                .map(Language::name)
                                .collect(Collectors.toSet()),
                        destino.getPunctuation(),
                        destino.getImageUrl(),
                        destino.getSecondaryImages()
                ))
                .toList();

        return ResponseEntity.ok(response_list);
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
    @PutMapping
    public ResponseEntity<Destino> uptade_destino(@RequestBody Destino destino){
        Optional<Destino> db_destino = destinoService.find_by_id(destino.getId());

        if(db_destino.isPresent()){
            return ResponseEntity.ok((destinoService.update_destino(destino)));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos incorrectos para destino");
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
