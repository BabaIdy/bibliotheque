package com.projet.bibliotheque.domain.enumeration;

public enum StatutEmprunt {
    ACTIF("Actif"),
    RETOURNE("Retourné"),
    RETOURNE_EN_RETARD("Retourné en retard"),
    ANNULE("Annulé");

    private final String libelle;

    StatutEmprunt(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
