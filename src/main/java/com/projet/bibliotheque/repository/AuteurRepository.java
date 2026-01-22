package com.projet.bibliotheque.repository;

import com.projet.bibliotheque.domain.Auteur;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Long> {
    @Query("SELECT a FROM Auteur a WHERE " +
        "LOWER(CONCAT(a.prenom, ' ', a.nom)) LIKE LOWER(CONCAT('%', :nomComplet, '%'))")
    List<Auteur> rechercherParNomComplet(@Param("nomComplet") String nomComplet);
    Optional<Auteur> findByNomAndPrenom(String nom, String prenom);
    List<Auteur> findAllByOrderByDateCreationDesc();
}
