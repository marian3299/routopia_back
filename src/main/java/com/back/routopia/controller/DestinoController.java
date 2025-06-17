package com.back.routopia.controller;

import com.back.routopia.entity.Destino;
import com.back.routopia.service.DestinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/destino")
@Tag(name = "Controller de Destinos", description = "CRUD de destinos")
public class DestinoController {

    @Autowired
    DestinoService destinoService;

    @Operation(summary = "Get all Destinos")
    @GetMapping
    public ResponseEntity<List<Destino>> find_all_destino(){
        return ResponseEntity.ok(destinoService.list_all());
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
    public ResponseEntity<Destino> create_destino (@RequestBody Destino destino){
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
