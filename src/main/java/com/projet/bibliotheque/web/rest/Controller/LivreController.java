package com.projet.bibliotheque.web.rest.Controller;

import com.projet.bibliotheque.service.LivreService;
import com.projet.bibliotheque.service.dto.LivreRequest;
import com.projet.bibliotheque.service.dto.LivreResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }
    @PostMapping
    public ResponseEntity<LivreResponse> creerLivre( @Valid @RequestBody LivreRequest request) {
        LivreResponse response = livreService.creerLivre(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<LivreResponse> modifierLivre(
        @PathVariable Long id,
        @RequestBody LivreRequest request) {
        LivreResponse response = livreService.modifierLivre(id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerLivre(@PathVariable Long id) {
        livreService.supprimerLivre(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<LivreResponse> obtenirLivre(@PathVariable Long id) {
        LivreResponse response = livreService.obtenirLivre(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LivreResponse>> obtenirTousLesLivres() {
        List<LivreResponse> livres = livreService.obtenirTousLesLivres();
        return ResponseEntity.ok(livres);
    }
    @GetMapping("/recherche")
    public ResponseEntity<List<LivreResponse>> rechercherLivres(
        @RequestParam("mot") String mot) {
        List<LivreResponse> livres = livreService.rechercherLivres(mot);
        return ResponseEntity.ok(livres);
    }
    @GetMapping("/disponibles")
    public ResponseEntity<List<LivreResponse>> obtenirLivresDisponibles() {
        List<LivreResponse> livres = livreService.obtenirLivresDisponibles();
        return ResponseEntity.ok(livres);
    }
}
