package com.back.routopia.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "destinos")
@Schema(description = "Entity que representa a un destino")
public class Destino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Valor unico por destino, autoincremental")
    private Long id;

    @Column
    private String name;
    @Column
    private Float precio;
    @Column
    private String duration_time;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column
    private String address;
    @Column
    private Float punctuation;
    @Column
    private String city;
    @Column(name = "image_url")
    private String imageUrl;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "destino_secondary_images", joinColumns = @JoinColumn(name = "destino_id"))
    @Column(name = "image_url")
    private List<String> secondaryImages;
    @ElementCollection(targetClass = Language.class)
    @CollectionTable(name = "destino_languages", joinColumns = @JoinColumn(name = "destino_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Set<Language> languages;

    public Destino() {

    }

    public Destino(Long id, String name, Float precio, String duration_time, String description, Category category, Set<Language> languages, String address, Float punctuation, String city, String imageUrl, List<String> secondaryImages) {
        this.id = id;
        this.name = name;
        this.precio = precio;
        this.duration_time = duration_time;
        this.description = description;
        this.category = category;
        this.languages = languages;
        this.address = address;
        this.punctuation = punctuation;
        this.city = city;
        this.imageUrl = imageUrl;
        this.secondaryImages = secondaryImages;
    }

    public Destino(String name, Float precio, String duration_time, Category category, String description, Set<Language> languages, String address, Float punctuation, String city, String imageUrl, List<String> secondaryImages) {
        this.name = name;
        this.precio = precio;
        this.duration_time = duration_time;
        this.category = category;
        this.description = description;
        this.languages = languages;
        this.address = address;
        this.punctuation = punctuation;
        this.city = city;
        this.imageUrl = imageUrl;
        this.secondaryImages = secondaryImages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(String duration_time) {
        this.duration_time = duration_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(Float punctuation) {
        this.punctuation = punctuation;
    }

    public String getCity() {
        return city;
    }

    public void setCtiy(String ctiy) {
        this.city = ctiy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSecondaryImages() {
        return secondaryImages;
    }

    public void setSecondaryImages(List<String> secondaryImages) {
        this.secondaryImages = secondaryImages;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
