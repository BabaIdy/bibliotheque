package com.projet.bibliotheque.domain.enumeration;

public enum RoleUtilisateur {
    ETUDIANT("Étudiant"),
    BIBLIOTHECAIRE("Bibliothécaire"),
    ADMINISTRATEUR("Administrateur");

    private final String libelle;

    RoleUtilisateur(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
