package com.projet.bibliotheque.web.rest.Controller;
import com.projet.bibliotheque.service.CategorieService;
import com.projet.bibliotheque.service.dto.CategorieRequest;
import com.projet.bibliotheque.service.dto.CategorieResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }
    @PostMapping
    public ResponseEntity<CategorieResponse> creerCategorie(@RequestBody CategorieRequest request) {
        CategorieResponse response = categorieService.creerCategorie(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategorieResponse> obtenirCategorie(@PathVariable Long id) {
        CategorieResponse response = categorieService.obtenirCategorie(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<CategorieResponse>> obtenirToutesLesCategories() {
        List<CategorieResponse> categories = categorieService.obtenirToutesLesCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<CategorieResponse>> rechercherCategories(
        @RequestParam("q") String recherche) {
        List<CategorieResponse> categories = categorieService.rechercherCategories(recherche);
        return ResponseEntity.ok(categories);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategorieResponse> modifierCategorie(
        @PathVariable Long id,
        @RequestBody CategorieRequest request) {
        CategorieResponse response = categorieService.modifierCategorie(id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCategorie(@PathVariable Long id) {
        categorieService.supprimerCategorie(id);
        return ResponseEntity.noContent().build();
    }
}
