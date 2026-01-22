package com.projet.bibliotheque.service.dto;

import java.time.LocalDateTime;

public class LivreResponse {
    private Long id;
    private String titre;
    private String isbn;
    private Integer anneePublication;
    private Integer nombreExemplairesTotal;
    private Integer nombreExemplairesDisponibles;
    private String statut;
    private AuteurSimpleResponse auteur;
    private CategorieSimpleResponse categorie;
    private Boolean disponible;
    private LocalDateTime dateCreation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(Integer anneePublication) {
        this.anneePublication = anneePublication;
    }

    public Integer getNombreExemplairesTotal() {
        return nombreExemplairesTotal;
    }

    public void setNombreExemplairesTotal(Integer nombreExemplairesTotal) {
        this.nombreExemplairesTotal = nombreExemplairesTotal;
    }

    public Integer getNombreExemplairesDisponibles() {
        return nombreExemplairesDisponibles;
    }

    public void setNombreExemplairesDisponibles(Integer nombreExemplairesDisponibles) {
        this.nombreExemplairesDisponibles = nombreExemplairesDisponibles;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public AuteurSimpleResponse getAuteur() {
        return auteur;
    }

    public void setAuteur(AuteurSimpleResponse auteur) {
        this.auteur = auteur;
    }

    public CategorieSimpleResponse getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieSimpleResponse categorie) {
        this.categorie = categorie;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
