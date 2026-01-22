package com.projet.bibliotheque.web.rest.Controller;
import com.projet.bibliotheque.service.AuteurService;
import com.projet.bibliotheque.service.dto.AuteurRequest;
import com.projet.bibliotheque.service.dto.AuteurResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auteurs")
public class AuteurController {
    private final AuteurService auteurService;
    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }
    @PostMapping
    public ResponseEntity<AuteurResponse> creerAuteur(@RequestBody AuteurRequest request) {
        AuteurResponse response = auteurService.creerAuteur(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> obtenirAuteur(@PathVariable Long id) {
        AuteurResponse response = auteurService.obtenirAuteur(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<AuteurResponse>> obtenirTousLesAuteurs() {
        List<AuteurResponse> auteurs = auteurService.obtenirTousLesAuteurs();
        return ResponseEntity.ok(auteurs);
    }
    @GetMapping("/recherche")
    public ResponseEntity<List<AuteurResponse>> rechercherAuteurs(
        @RequestParam("q") String recherche) {
        List<AuteurResponse> auteurs = auteurService.rechercherAuteurs(recherche);
        return ResponseEntity.ok(auteurs);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> modifierAuteur(
        @PathVariable Long id,
        @RequestBody AuteurRequest request) {
        AuteurResponse response = auteurService.modifierAuteur(id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAuteur(@PathVariable Long id) {
        auteurService.supprimerAuteur(id);
        return ResponseEntity.noContent().build();
    }
}
