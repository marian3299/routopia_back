package com.back.routopia.dto;

public class TraitDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private boolean deletable;

    public TraitDTO(Long id, String name, String imageUrl, boolean deletable) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.deletable = deletable;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

}
