package com.projet.bibliotheque.service;
import com.projet.bibliotheque.domain.Auteur;
import com.projet.bibliotheque.domain.Categorie;
import com.projet.bibliotheque.domain.Livre;
import com.projet.bibliotheque.domain.enumeration.StatutLivre;
import com.projet.bibliotheque.repository.AuteurRepository;
import com.projet.bibliotheque.repository.CategorieRepository;
import com.projet.bibliotheque.repository.LivreRepository;
import com.projet.bibliotheque.service.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projet.bibliotheque.domain.enumeration.StatutEmprunt;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class LivreService {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;
    private final CategorieRepository categorieRepository;

    public LivreService(LivreRepository livreRepository,
                        AuteurRepository auteurRepository,
                        CategorieRepository categorieRepository) {
        this.livreRepository = livreRepository;
        this.auteurRepository = auteurRepository;
        this.categorieRepository = categorieRepository;
    }

    public LivreResponse creerLivre(LivreRequest request) {
        if (livreRepository.existsByIsbn(request.getIsbn())) {
            throw new IllegalArgumentException("Un livre avec cet ISBN existe déjà");
        }

        Auteur auteur = auteurRepository.findById(request.getAuteurId())
            .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable"));

        Categorie categorie = categorieRepository.findById(request.getCategorieId())
            .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable"));

        Livre livre = new Livre();
        livre.setTitre(request.getTitre());
        livre.setIsbn(request.getIsbn());
        livre.setAnneePublication(request.getAnneePublication());
        livre.setExemplairesTotal(request.getNombreExemplaires());
        livre.setExemplairesDisponibles(request.getNombreExemplaires());
        livre.setStatut(StatutLivre.DISPONIBLE);
        livre.setAuteur(auteur);
        livre.setCategorie(categorie);

        Livre livreSauvegarde = livreRepository.save(livre);
        return mapToResponse(livreSauvegarde);
    }

    public LivreResponse modifierLivre(Long id, LivreRequest request) {
        Livre livre = livreRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Livre introuvable"));

        if (!livre.getIsbn().equals(request.getIsbn()) &&
            livreRepository.existsByIsbn(request.getIsbn())) {
            throw new IllegalArgumentException("Un livre avec cet ISBN existe déjà");
        }

        Auteur auteur = auteurRepository.findById(request.getAuteurId())
            .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable"));

        Categorie categorie = categorieRepository.findById(request.getCategorieId())
            .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable"));

        livre.setTitre(request.getTitre());
        livre.setIsbn(request.getIsbn());
        livre.setAnneePublication(request.getAnneePublication());
        livre.setAuteur(auteur);
        livre.setCategorie(categorie);

        return mapToResponse(livre);
    }

    @Transactional(readOnly = true)
    public LivreResponse obtenirLivre(Long id) {
        Livre livre = livreRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Livre introuvable"));
        return mapToResponse(livre);
    }

    @Transactional(readOnly = true)
    public List<LivreResponse> obtenirTousLesLivres() {
        return livreRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LivreResponse> rechercherLivres(String recherche) {
        return livreRepository.rechercherLivres(recherche).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LivreResponse> obtenirLivresDisponibles() {
        return livreRepository.findLivresDisponibles().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public void supprimerLivre(Long id) {
        Livre livre = livreRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Livre introuvable"));

        long empruntsActifs = livre.getEmprunts().stream()
            .filter(e -> e.getStatut() == StatutEmprunt.ACTIF)
            .count();

        if (empruntsActifs > 0) {
            throw new IllegalStateException(
                "Impossible de supprimer un livre ayant des emprunts actifs"
            );
        }

        livreRepository.delete(livre);
    }

    private LivreResponse mapToResponse(Livre livre) {
        LivreResponse response = new LivreResponse();
        response.setId(livre.getId());
        response.setTitre(livre.getTitre());
        response.setIsbn(livre.getIsbn());
        response.setAnneePublication(livre.getAnneePublication());
        response.setNombreExemplairesTotal(livre.getExemplairesTotal());
        response.setNombreExemplairesDisponibles(livre.getExemplairesDisponibles());
        response.setStatut(livre.getStatut().getLibelle());
        response.setDisponible(livre.estDisponible());
        response.setDateCreation(livre.getDateCreation());

        AuteurSimpleResponse auteurResponse = new AuteurSimpleResponse();
        auteurResponse.setId(livre.getAuteur().getId());
        auteurResponse.setNomComplet(livre.getAuteur().getNomComplet());
        response.setAuteur(auteurResponse);

        CategorieSimpleResponse categorieResponse = new CategorieSimpleResponse();
        categorieResponse.setId(livre.getCategorie().getId());
        categorieResponse.setNom(livre.getCategorie().getNom());
        response.setCategorie(categorieResponse);

        return response;
    }
}

