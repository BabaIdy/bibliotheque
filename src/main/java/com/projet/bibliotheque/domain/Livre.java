package com.projet.bibliotheque.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projet.bibliotheque.domain.enumeration.StatutLivre;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "livres")
@EntityListeners(AuditingEntityListener.class)
public class Livre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(nullable = false, length = 200)
    private String titre;

    @NotNull
    @Size(min = 10, max = 13)
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Min(1000)
    @Max(2100)
    private Integer anneePublication;

    @Min(0)
    private Integer exemplairesDisponibles;

    @NotNull
    @Min(1)
    @Max(1000)
    private Integer exemplairesTotal;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatutLivre statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    @JsonBackReference
    private Auteur auteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Emprunt> emprunts = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @LastModifiedDate
    private LocalDateTime dateModification;
    @PrePersist
    protected void onCreate() {
        if (exemplairesDisponibles == null) exemplairesDisponibles = exemplairesTotal;
        if (statut == null) statut = StatutLivre.DISPONIBLE;
        validerCoherence();
    }

    @PreUpdate
    protected void onUpdate() {
        validerCoherence();
    }

    private void validerCoherence() {
        if (exemplairesDisponibles != null && exemplairesTotal != null &&
            exemplairesDisponibles > exemplairesTotal) {
            throw new IllegalStateException("Les exemplaires disponibles ne peuvent pas dépasser le total");
        }
        if (anneePublication != null && anneePublication > LocalDate.now().getYear()) {
            throw new IllegalStateException("L'année de publication ne peut pas être dans le futur");
        }
    }

    public boolean estDisponible() {
        return exemplairesDisponibles != null && exemplairesDisponibles > 0 &&
            statut == StatutLivre.DISPONIBLE;
    }

    public void emprunter() {
        if (!estDisponible()) throw new IllegalStateException("Livre indisponible");
        exemplairesDisponibles--;
        if (exemplairesDisponibles == 0) statut = StatutLivre.EMPRUNTE;
    }

    public void retourner() {
        if (exemplairesDisponibles >= exemplairesTotal)
            throw new IllegalStateException("Tous les exemplaires sont déjà disponibles");
        exemplairesDisponibles++;
        statut = StatutLivre.DISPONIBLE;
    }
    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getIsbn() { return isbn; }
    public Integer getAnneePublication() { return anneePublication; }
    public Integer getExemplairesDisponibles() { return exemplairesDisponibles; }
    public Integer getExemplairesTotal() { return exemplairesTotal; }
    public StatutLivre getStatut() { return statut; }
    public Auteur getAuteur() { return auteur; }
    public Categorie getCategorie() { return categorie; }
    public Set<Emprunt> getEmprunts() { return emprunts; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }

    public void setId(Long id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAnneePublication(Integer anneePublication) { this.anneePublication = anneePublication; }
    public void setExemplairesDisponibles(Integer exemplairesDisponibles) { this.exemplairesDisponibles = exemplairesDisponibles; }
    public void setExemplairesTotal(Integer exemplairesTotal) { this.exemplairesTotal = exemplairesTotal; }
    public void setStatut(StatutLivre statut) { this.statut = statut; }
    public void setAuteur(Auteur auteur) { this.auteur = auteur; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public void setEmprunts(Set<Emprunt> emprunts) { this.emprunts = emprunts; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livre)) return false;
        Livre livre = (Livre) o;
        return id != null && Objects.equals(id, livre.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
