package com.projet.bibliotheque.service.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.*;
public class LivreRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 1, max = 255, message = "Le titre doit contenir entre 1 et 255 caractères")
    private String titre;

    @NotBlank(message = "L'ISBN est obligatoire")
     private String isbn;

    @NotNull(message = "L'année de publication est obligatoire")
    @Min(value = 1000, message = "L'année doit être supérieure à 1000")
    @Max(value = 2100, message = "L'année ne peut pas dépasser 2100")
    private Integer anneePublication;

    @NotNull(message = "Le nombre d'exemplaires est obligatoire")
    @Min(value = 1, message = "Il faut au moins 1 exemplaire")
    @Max(value = 1000, message = "Maximum 1000 exemplaires")
    private Integer nombreExemplaires;

    @NotNull(message = "L'ID de l'auteur est obligatoire")
    @Positive(message = "L'ID de l'auteur doit être positif")
    private Long auteurId;

    @NotNull(message = "L'ID de la catégorie est obligatoire")
    @Positive(message = "L'ID de la catégorie doit être positif")
    private Long categorieId;

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

    public Integer getNombreExemplaires() {
        return nombreExemplaires;
    }

    public void setNombreExemplaires(Integer nombreExemplaires) {
        this.nombreExemplaires = nombreExemplaires;
    }

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }
}
