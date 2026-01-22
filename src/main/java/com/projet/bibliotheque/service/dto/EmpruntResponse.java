package com.projet.bibliotheque.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmpruntResponse {
    private Long id;
    private UtilisateurSimpleResponse utilisateur;
    private LivreSimpleResponse livre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private String statut;
    private Boolean enRetard;
    private Long joursRetard;
    private String remarques;
    private LocalDateTime dateCreation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UtilisateurSimpleResponse getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurSimpleResponse utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LivreSimpleResponse getLivre() {
        return livre;
    }

    public void setLivre(LivreSimpleResponse livre) {
        this.livre = livre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getEnRetard() {
        return enRetard;
    }

    public void setEnRetard(Boolean enRetard) {
        this.enRetard = enRetard;
    }

    public Long getJoursRetard() {
        return joursRetard;
    }

    public void setJoursRetard(Long joursRetard) {
        this.joursRetard = joursRetard;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
