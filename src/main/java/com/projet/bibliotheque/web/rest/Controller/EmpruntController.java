package com.projet.bibliotheque.web.rest.Controller;

import com.projet.bibliotheque.service.EmpruntService;
import com.projet.bibliotheque.service.dto.EmpruntRequest;
import com.projet.bibliotheque.service.dto.EmpruntResponse;
import com.projet.bibliotheque.service.dto.RetourEmpruntRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprunts")
public class EmpruntController {
    private final EmpruntService empruntService;
    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }
    @PostMapping
    public ResponseEntity<EmpruntResponse> creerEmprunt(
        @Valid @RequestBody EmpruntRequest request) {
        EmpruntResponse emprunt = empruntService.creerEmprunt(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprunt);
    }
    @PostMapping("/{id}/retourner")
    public ResponseEntity<EmpruntResponse> retournerLivre(
        @PathVariable Long id,
        @Valid @RequestBody(required = false) RetourEmpruntRequest request) {
        if (request == null) {
            request = new RetourEmpruntRequest();
        }
        EmpruntResponse emprunt = empruntService.retournerLivre(id, request);
        return ResponseEntity.ok(emprunt);
    }
    @GetMapping("/retards")
    public ResponseEntity<List<EmpruntResponse>> obtenirEmpruntsEnRetard() {
        List<EmpruntResponse> emprunts = empruntService.obtenirEmpruntsEnRetard();
        return ResponseEntity.ok(emprunts);
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<EmpruntResponse>> obtenirEmpruntsUtilisateur(
        @PathVariable Long utilisateurId) {
        List<EmpruntResponse> emprunts = empruntService.obtenirEmpruntsUtilisateur(utilisateurId);
        return ResponseEntity.ok(emprunts);
    }

}
