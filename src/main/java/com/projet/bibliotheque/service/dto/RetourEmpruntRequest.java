package com.projet.bibliotheque.service.dto;

import jakarta.validation.constraints.Size;

public class RetourEmpruntRequest {

    @Size(max = 500, message = "Les remarques ne peuvent pas dépasser 500 caractères")
    private String remarques;

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}
