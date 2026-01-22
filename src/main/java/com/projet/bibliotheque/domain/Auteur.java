package com.projet.bibliotheque.domain;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(
    name = "auteurs",
    indexes = @Index(name = "idx_nom_prenom", columnList = "nom, prenom")
)
public class Auteur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
   // @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'-]+$")
    private String prenom;

    @NotBlank
    @Size(min = 2, max = 100)
    //@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'-]+$")
    private String nom;

    @NotNull
    @Past
    private LocalDate dateNaissance;

    @Size(max = 2000)
    @Column(columnDefinition = "TEXT")
    private String biographie;

    @OneToMany(
        mappedBy = "auteur",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    private List<Livre> livres = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    /* =================== CALLBACKS =================== */

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        normaliser();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
        normaliser();
    }

    private void normaliser() {
        prenom = prenom.trim();
        nom = nom.trim();
    }

    /* =================== MÉTIER =================== */

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        livre.setAuteur(this);
    }

    public void retirerLivre(Livre livre) {
        livres.remove(livre);
        livre.setAuteur(null);
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }

    /* =================== GETTERS =================== */

    public Long getId() { return id; }
    public List<Livre> getLivres() { return livres; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
