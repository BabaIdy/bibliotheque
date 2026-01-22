package com.projet.bibliotheque.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "categorie")
public class Categorie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Livre> livres = new HashSet<>();
    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        livre.setCategorie(this);
    }

    public void retirerLivre(Livre livre) {
        livres.remove(livre);
        livre.setCategorie(null);
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public Set<Livre> getLivres() { return livres; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setLivres(Set<Livre> livres) { this.livres = livres; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categorie)) return false;
        Categorie c = (Categorie) o;
        return id != null && Objects.equals(id, c.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
