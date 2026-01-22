package com.projet.bibliotheque.service.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class EmpruntRequest {

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    @Positive(message = "L'ID doit être positif")
    private Long utilisateurId;

    @NotNull(message = "L'ID du livre est obligatoire")
    @Positive(message = "L'ID doit être positif")
    private Long livreId;

    @Size(max = 500, message = "Les remarques ne peuvent pas dépasser 500 caractères")
    private String remarques;

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getLivreId() {
        return livreId;
    }

    public void setLivreId(Long livreId) {
        this.livreId = livreId;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}
