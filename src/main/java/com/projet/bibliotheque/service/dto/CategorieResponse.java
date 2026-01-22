package com.projet.bibliotheque.service.dto;

import java.time.LocalDateTime;

public class CategorieResponse {
    private Long id;
    private String nom;
    private String description;
    private Integer nombreLivres;
    private LocalDateTime dateCreation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNombreLivres() {
        return nombreLivres;
    }

    public void setNombreLivres(Integer nombreLivres) {
        this.nombreLivres = nombreLivres;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
