package com.projet.bibliotheque.service;
import com.projet.bibliotheque.domain.Auteur;
import com.projet.bibliotheque.repository.AuteurRepository;
import com.projet.bibliotheque.service.dto.AuteurRequest;
import com.projet.bibliotheque.service.dto.AuteurResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuteurService {
    private final AuteurRepository auteurRepository;
    public AuteurService(AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }
    public AuteurResponse creerAuteur(AuteurRequest request) {
        // Vérifier si l'auteur existe déjà
        if (auteurRepository.findByNomAndPrenom(request.getNom(), request.getPrenom()).isPresent()) {
            throw new IllegalArgumentException(
                "Un auteur avec ce nom et prénom existe déjà"
            );
        }

        Auteur auteur = new Auteur();
        auteur.setPrenom(request.getPrenom());
        auteur.setNom(request.getNom());
        auteur.setDateNaissance(request.getDateNaissance());
        auteur.setBiographie(request.getBiographie());

        Auteur auteurSauvegarde = auteurRepository.save(auteur);
        return mapToResponse(auteurSauvegarde);
    }

    public AuteurResponse modifierAuteur(Long id, AuteurRequest request) {
        Auteur auteur = auteurRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable"));

        // Vérifier si un autre auteur a le même nom/prénom
        auteurRepository.findByNomAndPrenom(request.getNom(), request.getPrenom())
            .ifPresent(a -> {
                if (!a.getId().equals(id)) {
                    throw new IllegalArgumentException(
                        "Un autre auteur avec ce nom et prénom existe déjà"
                    );
                }
            });

        auteur.setPrenom(request.getPrenom());
        auteur.setNom(request.getNom());
        auteur.setDateNaissance(request.getDateNaissance());
        auteur.setBiographie(request.getBiographie());

        return mapToResponse(auteur);
    }

    @Transactional(readOnly = true)
    public AuteurResponse obtenirAuteur(Long id) {
        Auteur auteur = auteurRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable"));
        return mapToResponse(auteur);
    }
   @Transactional(readOnly = true)
   public List<AuteurResponse> obtenirTousLesAuteurs() {
       return auteurRepository.findAllByOrderByDateCreationDesc().stream()
           .map(this::mapToResponse)
           .collect(Collectors.toList());
   }

    @Transactional(readOnly = true)
    public List<AuteurResponse> rechercherAuteurs(String recherche) {
        return auteurRepository.rechercherParNomComplet(recherche).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public void supprimerAuteur(Long id) {
        Auteur auteur = auteurRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable"));

        if (!auteur.getLivres().isEmpty()) {
            throw new IllegalStateException(
                "Impossible de supprimer un auteur ayant des livres associés"
            );
        }

        auteurRepository.delete(auteur);
    }

    private AuteurResponse mapToResponse(Auteur auteur) {
        AuteurResponse response = new AuteurResponse();
        response.setId(auteur.getId());
        response.setPrenom(auteur.getPrenom());
        response.setNom(auteur.getNom());
        response.setNomComplet(auteur.getNomComplet());
        response.setDateNaissance(auteur.getDateNaissance());
        response.setAge(auteur.getAge());
        response.setBiographie(auteur.getBiographie());
        response.setNombreLivres(auteur.getLivres() != null ? auteur.getLivres().size() : 0);
        response.setDateCreation(auteur.getDateCreation());
        return response;
    }
}
