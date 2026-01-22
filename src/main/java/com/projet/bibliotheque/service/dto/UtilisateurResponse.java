package com.projet.bibliotheque.service.dto;

import java.time.LocalDateTime;

public class UtilisateurResponse {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String role;
    private Boolean actif;
    private Long nombreEmpruntsActifs;
    private Boolean peutEmprunter;
    private LocalDateTime dateCreation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Long getNombreEmpruntsActifs() {
        return nombreEmpruntsActifs;
    }

    public void setNombreEmpruntsActifs(Long nombreEmpruntsActifs) {
        this.nombreEmpruntsActifs = nombreEmpruntsActifs;
    }

    public Boolean getPeutEmprunter() {
        return peutEmprunter;
    }

    public void setPeutEmprunter(Boolean peutEmprunter) {
        this.peutEmprunter = peutEmprunter;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
