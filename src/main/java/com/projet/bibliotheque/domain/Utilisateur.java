package com.projet.bibliotheque.domain;
import com.projet.bibliotheque.domain.enumeration.RoleUtilisateur;
import com.projet.bibliotheque.domain.enumeration.StatutEmprunt;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs", indexes = {
    @Index(name = "idx_email", columnList = "email")
})
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String prenom;
    @NotNull(message = "Le rôle est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleUtilisateur role;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Emprunt> emprunts = new ArrayList<>();

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    // Constructeurs
    public Utilisateur() {
    }

    public Utilisateur(Long id, String email, String nom, String prenom, RoleUtilisateur role) {
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.actif = true;
    }
    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public Boolean getActif() {
        return actif;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        normaliserDonnees();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
        normaliserDonnees();
    }

    private void normaliserDonnees() {
        if (email != null) {
            email = email.toLowerCase().trim();
        }
        if (nom != null) {
            nom = nom.trim();
        }
        if (prenom != null) {
            prenom = prenom.trim();
        }
    }

    public long getNombreEmpruntsActifs() {
        return emprunts.stream()
            .filter(e -> e.getStatut() == StatutEmprunt.ACTIF)
            .count();
    }

    public boolean peutEmprunter() {
        return actif && getNombreEmpruntsActifs() < 5;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

}
