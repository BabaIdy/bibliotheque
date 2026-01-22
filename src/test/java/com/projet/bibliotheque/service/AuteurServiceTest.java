package com.projet.bibliotheque.service;
import com.projet.bibliotheque.domain.Auteur;
import com.projet.bibliotheque.domain.Livre;
import com.projet.bibliotheque.repository.AuteurRepository;
import com.projet.bibliotheque.service.dto.AuteurRequest;
import com.projet.bibliotheque.service.dto.AuteurResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuteurServiceTest {
    @Mock
    private AuteurRepository auteurRepository;
    @InjectMocks
    private AuteurService auteurService;
    @Test
    void creerAuteur_devrait_creer_un_auteur_si_non_existant() {

        AuteurRequest request = new AuteurRequest();
        request.setNom("Diop");
        request.setPrenom("Cheikh");
        request.setDateNaissance(LocalDate.of(1960, 1, 1));
        request.setBiographie("Écrivain sénégalais");

        when(auteurRepository.findByNomAndPrenom("Diop", "Cheikh"))
            .thenReturn(Optional.empty());

        Auteur auteurSauvegarde = new Auteur();
        auteurSauvegarde.setId(1L);
        auteurSauvegarde.setNom("Diop");
        auteurSauvegarde.setPrenom("Cheikh");
        auteurSauvegarde.setDateNaissance(LocalDate.of(1960, 1, 1));
        auteurSauvegarde.setDateCreation(LocalDateTime.now());

        when(auteurRepository.save(any(Auteur.class)))
            .thenReturn(auteurSauvegarde);

        AuteurResponse response = auteurService.creerAuteur(request);

        assertNotNull(response);
        assertEquals("Diop", response.getNom());
        assertEquals("Cheikh", response.getPrenom());
        verify(auteurRepository).save(any(Auteur.class));
    }

    @Test
    void creerAuteur_devrait_echouer_si_auteur_existe_deja() {

        AuteurRequest request = new AuteurRequest();
        request.setNom("Diop");
        request.setPrenom("Cheikh");

        when(auteurRepository.findByNomAndPrenom("Diop", "Cheikh"))
            .thenReturn(Optional.of(new Auteur()));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> auteurService.creerAuteur(request)
        );

        assertEquals("Un auteur avec ce nom et prénom existe déjà", exception.getMessage());
        verify(auteurRepository, never()).save(any());
    }
    @Test
    void modifierAuteur_devrait_modifier_un_auteur_existant() {

        Auteur auteur = new Auteur();
        auteur.setId(1L);
        auteur.setNom("Ba");
        auteur.setPrenom("Mariama");
        auteur.setDateNaissance(LocalDate.of(1980, 5, 5));

        AuteurRequest request = new AuteurRequest();
        request.setNom("Ba");
        request.setPrenom("Mariama");
        request.setBiographie("Nouvelle biographie");
        request.setDateNaissance(LocalDate.of(1980, 5, 5));

        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));
        when(auteurRepository.findByNomAndPrenom("Ba", "Mariama")).thenReturn(Optional.of(auteur));

        AuteurResponse response = auteurService.modifierAuteur(1L, request);

        assertEquals("Ba", response.getNom());
        assertEquals("Mariama", response.getPrenom());
    }

    @Test
    void modifierAuteur_devrait_echouer_si_auteur_introuvable() {
        AuteurRequest request = new AuteurRequest();

        when(auteurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> auteurService.modifierAuteur(1L, request)
        );
    }
    @Test
    void obtenirAuteur_devrait_retourner_un_auteur_existant() {

        Auteur auteur = new Auteur();
        auteur.setId(1L);
        auteur.setNom("Fall");
        auteur.setPrenom("Aminata");
        auteur.setDateNaissance(LocalDate.of(1975, 3, 10));

        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));

        AuteurResponse response = auteurService.obtenirAuteur(1L);

        assertNotNull(response);
        assertEquals("Fall", response.getNom());
        assertEquals("Aminata", response.getPrenom());
    }
    @Test
    void obtenirTousLesAuteurs_devrait_retourner_les_auteurs_tries_par_date_creation_desc() {

        Auteur auteur = new Auteur();
        auteur.setId(1L);
        auteur.setNom("Sow");
        auteur.setPrenom("Ousmane");
        auteur.setDateNaissance(LocalDate.of(1985, 6, 15));

        when(auteurRepository.findAllByOrderByDateCreationDesc()).thenReturn(List.of(auteur));

        List<AuteurResponse> responses = auteurService.obtenirTousLesAuteurs();

        assertEquals(1, responses.size());
        verify(auteurRepository).findAllByOrderByDateCreationDesc();
    }

    @Test
    void supprimerAuteur_devrait_supprimer_un_auteur_sans_livres() {

        Auteur auteur = new Auteur();
        auteur.setId(1L);
        auteur.setLivres(Collections.emptyList());

        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));

        auteurService.supprimerAuteur(1L);

        verify(auteurRepository).delete(auteur);
    }

    @Test
    void supprimerAuteur_devrait_echouer_si_auteur_a_des_livres() {

        Auteur auteur = new Auteur();
        auteur.setId(1L);

        Livre livre = new Livre(); // vrai objet Livre
        livre.setId(1L);
        auteur.setLivres(List.of(livre));

        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));

        assertThrows(
            IllegalStateException.class,
            () -> auteurService.supprimerAuteur(1L)
        );

        verify(auteurRepository, never()).delete(any());
    }
}
