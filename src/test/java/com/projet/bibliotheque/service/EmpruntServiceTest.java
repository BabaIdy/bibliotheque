package com.projet.bibliotheque.service;
import com.projet.bibliotheque.domain.Emprunt;
import com.projet.bibliotheque.domain.Livre;
import com.projet.bibliotheque.domain.Utilisateur;
import com.projet.bibliotheque.domain.enumeration.StatutEmprunt;
import com.projet.bibliotheque.domain.enumeration.StatutLivre;
import com.projet.bibliotheque.repository.EmpruntRepository;
import com.projet.bibliotheque.repository.LivreRepository;
import com.projet.bibliotheque.repository.UtilisateurRepository;
import com.projet.bibliotheque.service.dto.EmpruntRequest;
import com.projet.bibliotheque.service.dto.EmpruntResponse;
import com.projet.bibliotheque.service.dto.RetourEmpruntRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpruntServiceTest {

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private EmpruntService empruntService;
    private Utilisateur creerUtilisateur(Long id, String prenom, String nom) {
        Utilisateur u = new Utilisateur();
        u.setId(id);
        u.setPrenom(prenom);
        u.setNom(nom);
        u.setActif(true);
        return u;
    }

    private Livre creerLivre(Long id, String titre, int exemplairesTotal, int exemplairesDisponibles) {
        Livre l = new Livre();
        l.setId(id);
        l.setTitre(titre);
        l.setExemplairesTotal(exemplairesTotal);
        l.setExemplairesDisponibles(exemplairesDisponibles);
        l.setStatut(StatutLivre.DISPONIBLE); // IMPORTANT: définir le statut
        return l;
    }

    private Emprunt creerEmprunt(Utilisateur u, Livre l, StatutEmprunt statut, LocalDate retourPrevue) {
        Emprunt e = new Emprunt();
        e.setUtilisateur(u);
        e.setLivre(l);
        e.setStatut(statut);
        e.setDateEmprunt(LocalDate.now());
        e.setDateRetourPrevue(retourPrevue);
        return e;
    }
    @Test
    void creerEmprunt_devrait_creer_emprunt_si_conditions_ok() {

        EmpruntRequest request = new EmpruntRequest();
        request.setUtilisateurId(1L);
        request.setLivreId(10L);
        request.setRemarques("Livre pour révision");

        Utilisateur utilisateur = creerUtilisateur(1L, "Mamadou", "Diop");
        Livre livre = creerLivre(10L, "Histoire du Sénégal", 5, 3);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(livreRepository.findById(10L)).thenReturn(Optional.of(livre));

        Emprunt empruntSauvegarde = creerEmprunt(utilisateur, livre, StatutEmprunt.ACTIF, LocalDate.now().plusDays(14));
        empruntSauvegarde.setId(100L);

        when(empruntRepository.save(any(Emprunt.class))).thenReturn(empruntSauvegarde);

        EmpruntResponse response = empruntService.creerEmprunt(request);

        assertNotNull(response);
        assertEquals("Actif", response.getStatut());
        assertEquals(livre.getId(), response.getLivre().getId());
        assertEquals(utilisateur.getId(), response.getUtilisateur().getId());
        verify(empruntRepository).save(any(Emprunt.class));
    }

    @Test
    void creerEmprunt_devrait_echouer_si_utilisateur_introuvable() {
        EmpruntRequest request = new EmpruntRequest();
        request.setUtilisateurId(1L);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> empruntService.creerEmprunt(request));
    }

    @Test
    void creerEmprunt_devrait_echouer_si_utilisateur_ne_peut_pas_emprunter() {
        Utilisateur utilisateur = creerUtilisateur(1L, "Mamadou", "Diop");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        EmpruntRequest request = new EmpruntRequest();
        request.setUtilisateurId(1L);
        request.setLivreId(10L);
        Livre livre = creerLivre(10L, "Histoire du Sénégal", 5, 1);
        for (int i = 0; i < 5; i++) {
            Emprunt e = creerEmprunt(utilisateur, livre, StatutEmprunt.ACTIF, LocalDate.now().plusDays(10));
            utilisateur.getEmprunts().add(e);
        }

        assertThrows(IllegalStateException.class,
            () -> empruntService.creerEmprunt(request));
    }
    @Test
    void creerEmprunt_devrait_echouer_si_livre_non_disponible() {
        Utilisateur utilisateur = creerUtilisateur(1L, "Mamadou", "Diop");
        // Livre sans exemplaires disponibles
        Livre livre = creerLivre(10L, "Histoire du Sénégal", 5, 0);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(livreRepository.findById(10L)).thenReturn(Optional.of(livre));

        EmpruntRequest request = new EmpruntRequest();
        request.setUtilisateurId(1L);
        request.setLivreId(10L);

        assertThrows(IllegalStateException.class,
            () -> empruntService.creerEmprunt(request));
    }

    @Test
    void retournerLivre_devrait_retourner_emprunt_et_livre() {
        Utilisateur utilisateur = creerUtilisateur(1L, "Mamadou", "Diop");
        Livre livre = creerLivre(10L, "Histoire du Sénégal", 5, 4);
        Emprunt emprunt = creerEmprunt(utilisateur, livre, StatutEmprunt.ACTIF, LocalDate.now().plusDays(7));

        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));

        RetourEmpruntRequest request = new RetourEmpruntRequest();
        request.setRemarques("Livre rendu en bon état");

        EmpruntResponse response = empruntService.retournerLivre(1L, request);

        assertNotNull(response);
      assertEquals("Retourné", response.getStatut());
    }
    @Test
    void obtenirEmpruntsEnRetard_devrait_retourner_liste() {
        Utilisateur utilisateur = creerUtilisateur(1L, "Mamadou", "Diop");
        Livre livre = creerLivre(10L, "Histoire du Sénégal", 5, 1);

        Emprunt emprunt = creerEmprunt(utilisateur, livre, StatutEmprunt.ACTIF, LocalDate.now().minusDays(1));

        when(empruntRepository.findEmpruntsEnRetard(any(LocalDate.class)))
            .thenReturn(List.of(emprunt));

        List<EmpruntResponse> responses = empruntService.obtenirEmpruntsEnRetard();

        assertEquals(1, responses.size());
    }

    @Test
    void obtenirEmpruntsUtilisateur_devrait_retourner_emprunts_tries() {
        Utilisateur utilisateur = creerUtilisateur(1L, "Mamadou", "Diop");
        Livre livre = creerLivre(10L, "Histoire du Sénégal", 5, 1);

        Emprunt emprunt = creerEmprunt(utilisateur, livre, StatutEmprunt.ACTIF, LocalDate.now().plusDays(5));

        when(empruntRepository.findByUtilisateurIdOrderByDateCreationDesc(1L))
            .thenReturn(List.of(emprunt));

        List<EmpruntResponse> responses = empruntService.obtenirEmpruntsUtilisateur(1L);

        assertEquals(1, responses.size());
        verify(empruntRepository).findByUtilisateurIdOrderByDateCreationDesc(1L);
    }

    @Test
    void obtenirTousLesEmprunts_devrait_retourner_tous_les_emprunts() {
        Utilisateur utilisateur = creerUtilisateur(1L, "Mamadou", "Diop");
        Livre livre = creerLivre(10L, "Histoire du Sénégal", 5, 1);

        Emprunt emprunt = creerEmprunt(utilisateur, livre, StatutEmprunt.ACTIF, LocalDate.now().plusDays(5));

        when(empruntRepository.findAllByOrderByDateCreationDesc())
            .thenReturn(List.of(emprunt));

        List<EmpruntResponse> responses = empruntService.obtenirTousLesEmprunts();

        assertEquals(1, responses.size());
        verify(empruntRepository).findAllByOrderByDateCreationDesc();
    }
}
