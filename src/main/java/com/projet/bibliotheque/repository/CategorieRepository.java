package com.projet.bibliotheque.repository;

import com.projet.bibliotheque.domain.Categorie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    List<Categorie> findByNomContainingIgnoreCase(String nom);

    boolean existsByNom(String nom);
}
