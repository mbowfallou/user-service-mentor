package sn.groupeisi.us.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.FiliereDto;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.service.FiliereDomaineService;

import java.util.List;

@RestController
@RequestMapping("/filiere")
public class FiliereController {

    private final FiliereDomaineService filiereDomaineService;

    public FiliereController(FiliereDomaineService filiereDomaineService) {
        this.filiereDomaineService = filiereDomaineService;
    }

    // Ajouter une nouvelle filière
    @PostMapping
    public ResponseEntity<FiliereDto> addFiliere(@RequestBody String nom) {
        FiliereDto filiere = filiereDomaineService.addFiliere(nom);
        return ResponseEntity.status(HttpStatus.CREATED).body(filiere);
    }

    // Récupérer une filière par ID
    @GetMapping("/{id}")
    public ResponseEntity<FiliereDto> getFiliere(@PathVariable Long id) {
        FiliereDto filiere = filiereDomaineService.getFiliere(id);
        return ResponseEntity.ok(filiere);
    }

    // Récupérer une filière par son nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<?> getFiliereByNom(@PathVariable String nom) {
        try {
            FiliereDto filiere = filiereDomaineService.findFiliereByNom(nom);
            return ResponseEntity.ok(filiere);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Récupérer toutes les filières
    @GetMapping
    public ResponseEntity<List<FiliereDto>> findAllFilieres() {
        List<FiliereDto> filieres = filiereDomaineService.findAllFilieres();
        return ResponseEntity.ok(filieres);
    }

    // Supprimer une filière par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(@PathVariable Long id) {
        filiereDomaineService.deleteFiliere(id);
        return ResponseEntity.noContent().build();
    }
}
