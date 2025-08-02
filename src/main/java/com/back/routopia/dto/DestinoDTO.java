package com.back.routopia.dto;

import com.back.routopia.entity.Language;

import java.util.Set;
import java.util.List;

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
    private String imageUrl;
    private List<String> secondaryImages;

    public DestinoDTO(String name, Long id, String category, String city, Float precio, String duration_time, String description, String address, Set<String> languages, Float punctuation, String imageUrl, List<String> secondaryImages) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.city = city;
        this.location = city + ", " + category_to_country(category);
        this.precio = precio;
        this.duration_time = duration_time;
        this.description = description;
        this.address = address;
        this.languages = languages;
        this.punctuation = punctuation;
        this.imageUrl = imageUrl;
        this.secondaryImages = secondaryImages;
    }

    private String category_to_country(String category){
        return switch (category){
            case "FRANCE" -> "Francia";
            case "JAPAN" -> "Japón";
            case "THAILAND" -> "Tailandia";
            case "GREECE" -> "Grecia";
            case "MEXICO" -> "Mexico";
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

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getSecondaryImages() {
        return secondaryImages;
    }
}
