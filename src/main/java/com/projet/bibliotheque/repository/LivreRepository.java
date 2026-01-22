package com.projet.bibliotheque.repository;
import com.projet.bibliotheque.domain.Livre;
import com.projet.bibliotheque.domain.enumeration.StatutLivre;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
    boolean existsByIsbn(String isbn);
    @Query("""
        SELECT l FROM Livre l
        WHERE LOWER(l.titre) LIKE LOWER(CONCAT('%', :recherche, '%'))
           OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :recherche, '%'))
           OR LOWER(l.auteur.nom) LIKE LOWER(CONCAT('%', :recherche, '%'))
    """)
    List<Livre> rechercherLivres(@Param("recherche") String recherche);

    @Query("""
        SELECT l FROM Livre l
        WHERE l.exemplairesDisponibles > 0
          AND l.statut = :statut
    """)
    List<Livre> findLivresDisponibles();

}
