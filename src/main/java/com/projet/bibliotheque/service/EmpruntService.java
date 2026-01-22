package com.projet.bibliotheque.service;
import com.projet.bibliotheque.domain.Emprunt;
import com.projet.bibliotheque.domain.Livre;
import com.projet.bibliotheque.domain.Utilisateur;
import com.projet.bibliotheque.domain.enumeration.StatutEmprunt;
import com.projet.bibliotheque.repository.EmpruntRepository;
import com.projet.bibliotheque.repository.LivreRepository;
import com.projet.bibliotheque.repository.UtilisateurRepository;
import com.projet.bibliotheque.service.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LivreRepository livreRepository;

    public EmpruntService(EmpruntRepository empruntRepository,
                          UtilisateurRepository utilisateurRepository,
                          LivreRepository livreRepository) {
        this.empruntRepository = empruntRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.livreRepository = livreRepository;
    }

    public EmpruntResponse creerEmprunt(EmpruntRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        if (!utilisateur.peutEmprunter()) {
            throw new IllegalStateException(
                "L'utilisateur a atteint la limite d'emprunts (5) ou est inactif"
            );
        }

        Livre livre = livreRepository.findById(request.getLivreId())
            .orElseThrow(() -> new IllegalArgumentException("Livre introuvable"));

        if (!livre.estDisponible()) {
            throw new IllegalStateException(
                "Le livre n'est pas disponible pour l'emprunt"
            );
        }

        livre.emprunter();
        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setLivre(livre);
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(14));
        emprunt.setStatut(StatutEmprunt.ACTIF);
        emprunt.setRemarques(request.getRemarques());

        Emprunt empruntSauvegarde = empruntRepository.save(emprunt);
        return mapToResponse(empruntSauvegarde);
    }

    public EmpruntResponse retournerLivre(Long empruntId, RetourEmpruntRequest request) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
            .orElseThrow(() -> new IllegalArgumentException("Emprunt introuvable"));

        emprunt.retourner();

        if (request != null && request.getRemarques() != null) {
            String remarques = emprunt.getRemarques();
            emprunt.setRemarques(
                remarques != null ? remarques + " | " + request.getRemarques()
                    : request.getRemarques()
            );
        }

        emprunt.getLivre().retourner();

        return mapToResponse(emprunt);
    }
    @Transactional(readOnly = true)
    public List<EmpruntResponse> obtenirEmpruntsEnRetard() {
        return empruntRepository.findEmpruntsEnRetard(LocalDate.now()).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

   /* @Transactional(readOnly = true)
    public List<EmpruntResponse> obtenirEmpruntsUtilisateur(Long utilisateurId) {
        return empruntRepository.findByUtilisateurId(utilisateurId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }*/
   @Transactional(readOnly = true)
   public List<EmpruntResponse> obtenirEmpruntsUtilisateur(Long utilisateurId) {
       return empruntRepository.findByUtilisateurIdOrderByDateCreationDesc(utilisateurId).stream()
           .map(this::mapToResponse)
           .collect(Collectors.toList());
   }
    @Transactional(readOnly = true)
    public List<EmpruntResponse> obtenirTousLesEmprunts() {
        return empruntRepository.findAllByOrderByDateCreationDesc().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    private EmpruntResponse mapToResponse(Emprunt emprunt) {
        EmpruntResponse response = new EmpruntResponse();
        response.setId(emprunt.getId());
        response.setDateEmprunt(emprunt.getDateEmprunt());
        response.setDateRetourPrevue(emprunt.getDateRetourPrevue());
        response.setDateRetourEffective(emprunt.getDateRetourEffective());
        response.setStatut(emprunt.getStatut().getLibelle());
        response.setEnRetard(emprunt.estEnRetard());
        response.setJoursRetard(emprunt.getJoursRetard());
        response.setRemarques(emprunt.getRemarques());
        response.setDateCreation(emprunt.getDateCreation());

        UtilisateurSimpleResponse utilisateurResponse = new UtilisateurSimpleResponse();
        utilisateurResponse.setId(emprunt.getUtilisateur().getId());
        utilisateurResponse.setNomComplet(emprunt.getUtilisateur().getNomComplet());
        utilisateurResponse.setEmail(emprunt.getUtilisateur().getEmail());
        response.setUtilisateur(utilisateurResponse);

        LivreSimpleResponse livreResponse = new LivreSimpleResponse();
        livreResponse.setId(emprunt.getLivre().getId());
        livreResponse.setTitre(emprunt.getLivre().getTitre());
        livreResponse.setIsbn(emprunt.getLivre().getIsbn());
        response.setLivre(livreResponse);

        return response;
    }
}
