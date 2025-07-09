package com.back.routopia.dto;

import com.back.routopia.entity.Language;

import java.util.Set;

public class DestinoDTO {
    private Long id;
    private String name;
    private String city;
    private String category;
    private String location;
    private Float precio;
    private String duration_time;
    private String description;
    private Set<String> languages;
    private String address;
    private Float punctuation;

    public DestinoDTO(String name, Long id, String category, String city, Float precio, String duration_time, String description, String address, Set<String> languages, Float punctuation) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.city = city;
        this.location = city + ", " + category_to_country(category);;
        this.precio = precio;
        this.duration_time = duration_time;
        this.description = description;
        this.address = address;
        this.languages = languages;
        this.punctuation = punctuation;
    }

    private String category_to_country(String category){
        return switch (category){
            case "FRANCE" -> "Francia";
            case "JAPAN" -> "Japón";
            case "THAILAND" -> "Tailandia";
            case "GREECE" -> "Grecia";
            case "CHIAPAS" -> "México";
            default -> "Desconocido";
        };
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getDuration_time() {
        return duration_time;
    }

    public Float getPrecio() {
        return precio;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public Float getPunctuation() {
        return punctuation;
    }
}
