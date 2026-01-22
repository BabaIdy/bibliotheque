package com.projet.bibliotheque.service;
import com.projet.bibliotheque.domain.Categorie;
import com.projet.bibliotheque.domain.Livre;
import com.projet.bibliotheque.repository.CategorieRepository;
import com.projet.bibliotheque.service.dto.CategorieRequest;
import com.projet.bibliotheque.service.dto.CategorieResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategorieServiceTest {

    @Mock
    private CategorieRepository categorieRepository;

    @InjectMocks
    private CategorieService categorieService;
    // creerCategorie

    @Test
    void creerCategorie_devrait_creer_une_categorie_si_nom_unique() {

        // GIVEN
        CategorieRequest request = new CategorieRequest();
        request.setNom("Littérature Africaine");
        request.setDescription("Ouvrages d'auteurs africains");

        when(categorieRepository.existsByNom("Littérature Africaine"))
            .thenReturn(false);

        Categorie categorieSauvegardee = new Categorie();
        categorieSauvegardee.setId(1L);
        categorieSauvegardee.setNom("Littérature Africaine");
        categorieSauvegardee.setDescription("Ouvrages d'auteurs africains");

        when(categorieRepository.save(any(Categorie.class)))
            .thenReturn(categorieSauvegardee);

        // WHEN
        CategorieResponse response = categorieService.creerCategorie(request);

        // THEN
        assertNotNull(response);
        assertEquals("Littérature Africaine", response.getNom());
        verify(categorieRepository).save(any(Categorie.class));
    }

    @Test
    void creerCategorie_devrait_echouer_si_nom_existe_deja() {

        // GIVEN
        CategorieRequest request = new CategorieRequest();
        request.setNom("Histoire");

        when(categorieRepository.existsByNom("Histoire"))
            .thenReturn(true);

        // WHEN + THEN
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> categorieService.creerCategorie(request)
        );

        assertEquals(
            "Une catégorie avec ce nom existe déjà",
            exception.getMessage()
        );

        verify(categorieRepository, never()).save(any());
    }
    // modifierCategorie

    @Test
    void modifierCategorie_devrait_modifier_categorie_existante() {

        // GIVEN
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        categorie.setNom("Sciences");
        categorie.setDescription("Ancienne description");

        CategorieRequest request = new CategorieRequest();
        request.setNom("Sciences Modernes");
        request.setDescription("Nouvelle description");

        when(categorieRepository.findById(1L))
            .thenReturn(Optional.of(categorie));

        when(categorieRepository.existsByNom("Sciences Modernes"))
            .thenReturn(false);

        // WHEN
        CategorieResponse response = categorieService.modifierCategorie(1L, request);

        // THEN
        assertEquals("Sciences Modernes", response.getNom());
        assertEquals("Nouvelle description", response.getDescription());
    }

    @Test
    void modifierCategorie_devrait_echouer_si_categorie_introuvable() {

        // GIVEN
        CategorieRequest request = new CategorieRequest();

        when(categorieRepository.findById(1L))
            .thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(
            IllegalArgumentException.class,
            () -> categorieService.modifierCategorie(1L, request)
        );
    }

    @Test
    void modifierCategorie_devrait_echouer_si_nom_deja_utilise_par_autre_categorie() {

        // GIVEN
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        categorie.setNom("Économie");

        CategorieRequest request = new CategorieRequest();
        request.setNom("Politique");

        when(categorieRepository.findById(1L))
            .thenReturn(Optional.of(categorie));

        when(categorieRepository.existsByNom("Politique"))
            .thenReturn(true);

        // WHEN + THEN
        assertThrows(
            IllegalArgumentException.class,
            () -> categorieService.modifierCategorie(1L, request)
        );
    }
    // obtenirCategorie
    @Test
    void obtenirCategorie_devrait_retourner_categorie_existante() {

        // GIVEN
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        categorie.setNom("Géographie");

        when(categorieRepository.findById(1L))
            .thenReturn(Optional.of(categorie));

        // WHEN
        CategorieResponse response = categorieService.obtenirCategorie(1L);

        // THEN
        assertNotNull(response);
        assertEquals("Géographie", response.getNom());
    }
    // obtenirToutesLesCategories

    @Test
    void obtenirToutesLesCategories_devrait_retourner_liste_categories() {

        // GIVEN
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        categorie.setNom("Philosophie");

        when(categorieRepository.findAll())
            .thenReturn(List.of(categorie));
        // WHEN
        List<CategorieResponse> responses = categorieService.obtenirToutesLesCategories();

        // THEN
        assertEquals(1, responses.size());
        verify(categorieRepository).findAll();
    }
    // rechercherCategories

    @Test
    void rechercherCategories_devrait_retourner_categories_correspondantes() {

        // GIVEN
        Categorie categorie = new Categorie();
        categorie.setNom("Histoire du Sénégal");

        when(categorieRepository.findByNomContainingIgnoreCase("sénégal"))
            .thenReturn(List.of(categorie));

        // WHEN
        List<CategorieResponse> responses =
            categorieService.rechercherCategories("sénégal");

        // THEN
        assertEquals(1, responses.size());
        assertEquals("Histoire du Sénégal", responses.get(0).getNom());
    }
    // supprimerCategorie

    @Test
    void supprimerCategorie_devrait_echouer_si_categorie_a_des_livres() {
        // GIVEN
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        Livre livre = mock(Livre.class);
        Set<Livre> livres = new HashSet<>();
        livres.add(livre);
        categorie.setLivres(livres);
        when(categorieRepository.findById(1L))
            .thenReturn(Optional.of(categorie));

        // WHEN + THEN
        assertThrows(
            IllegalStateException.class,
            () -> categorieService.supprimerCategorie(1L)
        );

        verify(categorieRepository, never()).delete(any());
    }
    @Test
    void supprimerCategorie_devrait_supprimer_categorie_sans_livres() {

        // GIVEN
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        categorie.setLivres(new HashSet<>());
        when(categorieRepository.findById(1L))
            .thenReturn(Optional.of(categorie));

        // WHEN
        categorieService.supprimerCategorie(1L);

        // THEN
        verify(categorieRepository).delete(categorie);
    }
}
