package com.back.routopia.controller;

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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/destino")
@CrossOrigin(origins = "*")
@Tag(name = "Controller de Destinos", description = "CRUD de destinos")
public class DestinoController {

    @Autowired
    DestinoService destinoService;

    @Operation(summary = "Get all Destinos")
    @GetMapping
    public ResponseEntity<List<DestinoDTO>> find_all_destino(){
        List<Destino> destinos = destinoService.list_all();

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
                        destino.getLanguage().name(),
                        destino.getPunctuation()
                ))
                .toList();

        return ResponseEntity.ok(response_list);
    }

    @Operation(summary = "Get Destino by id")
    @GetMapping("/{id}")
    public ResponseEntity<Destino> find_destino_by_id(@PathVariable("id") Long id) {
        Destino destino = destinoService.find_by_id(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado"));

        return ResponseEntity.ok(destino);
    }

    @Operation(summary = "Crear destino")
    @PostMapping
    public ResponseEntity<Destino> create_destino (
            @RequestParam("name") String name,
            @RequestParam("price") Float price,
            @RequestParam("duration") String duration,
            @RequestParam("description") String description,
            @RequestParam("language") String language,
            @RequestParam("location") String location,
            @RequestParam("category") String category,
            @RequestParam("score") Float score,
            @RequestParam("city") String city,
            @RequestParam(value = "image", required = false) MultipartFile image
    ){
        Destino destino = new Destino();
        destino.setName(name);
        destino.setPrecio(price);
        destino.setDuration_time(duration);
        destino.setDescription(description);
        destino.setLanguage(Language.valueOf(language));
        destino.setAddress(location);
        destino.setCategory(Category.valueOf(category));
        destino.setPunctuation(score);
        destino.setCity(city);

        if (image != null && !image.isEmpty()) {
            // Por ahora solo guardamos el nombre del archivo
            // Más adelante puedes implementar la subida a S3
            destino.setImageUrl(image.getOriginalFilename());
        }

        return ResponseEntity.ok(destinoService.create_destino(destino));
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
