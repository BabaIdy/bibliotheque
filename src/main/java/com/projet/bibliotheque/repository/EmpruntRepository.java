package com.projet.bibliotheque.repository;
import com.projet.bibliotheque.domain.Emprunt;
import com.projet.bibliotheque.domain.enumeration.StatutEmprunt;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    @Query("SELECT e FROM Emprunt e WHERE e.statut = 'ACTIF' " +
        "AND e.dateRetourPrevue < :date")
    List<Emprunt> findEmpruntsEnRetard(@Param("date") LocalDate date);
    List<Emprunt> findAllByOrderByDateCreationDesc();
    List<Emprunt> findByUtilisateurIdOrderByDateCreationDesc(Long utilisateurId);
}
