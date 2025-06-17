package com.back.routopia.service;

import com.back.routopia.entity.Destino;
import com.back.routopia.repositroy.DestinoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinoService {
    @Autowired
    private DestinoRespository destinoRespository;

    public Destino create_destino(Destino destino){
        return destinoRespository.save(destino);
    }

    public List<Destino> list_all() { return destinoRespository.findAll(); }

    public Optional<Destino> find_by_id(Long id) { return destinoRespository.findById(id); }

    public void delete_destino(Long id) { destinoRespository.deleteById(id); }

    public Destino update_destino (Destino destino){ return destinoRespository.save(destino); }
}
