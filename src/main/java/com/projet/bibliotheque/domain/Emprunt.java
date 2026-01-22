package com.projet.bibliotheque.domain;
import com.projet.bibliotheque.domain.enumeration.StatutEmprunt;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@Entity
@Table(name = "emprunts")
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;

    @NotNull
    @PastOrPresent
    private LocalDate dateEmprunt;

    @NotNull
    @Future
    private LocalDate dateRetourPrevue;

    private LocalDate dateRetourEffective;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatutEmprunt statut;

    @Column(length = 500)
    private String remarques;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    public void retourner() {
        if (statut != StatutEmprunt.ACTIF) {
            throw new IllegalStateException(
                "Cet emprunt ne peut pas être retourné (statut: " + statut + ")"
            );
        }
        this.dateRetourEffective = LocalDate.now();
        if (estEnRetard()) {
            statut = StatutEmprunt.RETOURNE_EN_RETARD;
        } else {
            statut = StatutEmprunt.RETOURNE;
        }
    }

    public boolean estEnRetard() {
        return dateRetourEffective == null && LocalDate.now().isAfter(dateRetourPrevue);
    }

    public long getJoursRetard() {
        if (!estEnRetard()) return 0;
        return ChronoUnit.DAYS.between(dateRetourPrevue, LocalDate.now());
    }
    public Long getId() { return id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public Livre getLivre() { return livre; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public StatutEmprunt getStatut() { return statut; }
    public String getRemarques() { return remarques; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }

    public void setId(Long id) { this.id = id; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public void setLivre(Livre livre) { this.livre = livre; }
    public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }
    public void setStatut(StatutEmprunt statut) { this.statut = statut; }
    public void setRemarques(String remarques) { this.remarques = remarques; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emprunt)) return false;
        Emprunt emprunt = (Emprunt) o;
        return id != null && id.equals(emprunt.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Emprunt{" +
            "id=" + id +
            ", utilisateur=" + (utilisateur != null ? utilisateur.getId() : null) +
            ", livre=" + (livre != null ? livre.getId() : null) +
            ", dateEmprunt=" + dateEmprunt +
            ", dateRetourPrevue=" + dateRetourPrevue +
            ", dateRetourEffective=" + dateRetourEffective +
            ", statut=" + statut +
            '}';
    }
}
