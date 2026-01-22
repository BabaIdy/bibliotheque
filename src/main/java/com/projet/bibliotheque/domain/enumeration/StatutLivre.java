package com.projet.bibliotheque.domain.enumeration;

public enum StatutLivre {
    DISPONIBLE("Disponible"),
    EMPRUNTE("Emprunté"),
    EN_MAINTENANCE("En maintenance"),
    PERDU("Perdu"),
    RETIRE("Retiré");

    private final String libelle;

    StatutLivre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
