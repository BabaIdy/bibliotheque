package com.projet.bibliotheque.service;
import com.projet.bibliotheque.domain.Categorie;
import com.projet.bibliotheque.repository.CategorieRepository;
import com.projet.bibliotheque.service.dto.CategorieRequest;
import com.projet.bibliotheque.service.dto.CategorieResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public CategorieResponse creerCategorie(CategorieRequest request) {
        // Vérifier si la catégorie existe déjà
        if (categorieRepository.existsByNom(request.getNom())) {
            throw new IllegalArgumentException(
                "Une catégorie avec ce nom existe déjà"
            );
        }

        Categorie categorie = new Categorie();
        categorie.setNom(request.getNom());
        categorie.setDescription(request.getDescription());

        Categorie categorieSauvegarde = categorieRepository.save(categorie);
        return mapToResponse(categorieSauvegarde);
    }

    public CategorieResponse modifierCategorie(Long id, CategorieRequest request) {
        Categorie categorie = categorieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable"));

        // Vérifier si une autre catégorie a le même nom
        if (!categorie.getNom().equals(request.getNom()) &&
            categorieRepository.existsByNom(request.getNom())) {
            throw new IllegalArgumentException(
                "Une catégorie avec ce nom existe déjà"
            );
        }

        categorie.setNom(request.getNom());
        categorie.setDescription(request.getDescription());

        return mapToResponse(categorie);
    }

    @Transactional(readOnly = true)
    public CategorieResponse obtenirCategorie(Long id) {
        Categorie categorie = categorieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable"));
        return mapToResponse(categorie);
    }

    @Transactional(readOnly = true)
    public List<CategorieResponse> obtenirToutesLesCategories() {
        return categorieRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategorieResponse> rechercherCategories(String recherche) {
        return categorieRepository.findByNomContainingIgnoreCase(recherche).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public void supprimerCategorie(Long id) {
        Categorie categorie = categorieRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable"));

        if (!categorie.getLivres().isEmpty()) {
            throw new IllegalStateException(
                "Impossible de supprimer une catégorie ayant des livres associés"
            );
        }

        categorieRepository.delete(categorie);
    }

    private CategorieResponse mapToResponse(Categorie categorie) {
        CategorieResponse response = new CategorieResponse();
        response.setId(categorie.getId());
        response.setNom(categorie.getNom());
        response.setDescription(categorie.getDescription());
        return response;
    }
}
