package com.projet.bibliotheque.web.rest.Controller;
import com.projet.bibliotheque.domain.Utilisateur;
import com.projet.bibliotheque.service.UtilisateurService;
import com.projet.bibliotheque.service.dto.UtilisateurRequest;
import com.projet.bibliotheque.service.dto.UtilisateurResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping
    public ResponseEntity<UtilisateurResponse> creerUtilisateur(
        @Valid @RequestBody UtilisateurRequest request) {
        Utilisateur utilisateur = utilisateurService.creerUtilisateur(request);
        UtilisateurResponse response = utilisateurService.obtenirUtilisateur(utilisateur.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurResponse>> obtenirTousLesUtilisateurs() {
        List<UtilisateurResponse> utilisateurs = utilisateurService.obtenirTousLesUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponse> obtenirUtilisateur(@PathVariable Long id) {
        UtilisateurResponse utilisateur = utilisateurService.obtenirUtilisateur(id);
        return ResponseEntity.ok(utilisateur);
    }
}
