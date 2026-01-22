package com.projet.bibliotheque.repository;
import com.projet.bibliotheque.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    boolean existsByEmail(String email);
    List<Utilisateur> findAllByOrderByDateCreationDesc();

}
