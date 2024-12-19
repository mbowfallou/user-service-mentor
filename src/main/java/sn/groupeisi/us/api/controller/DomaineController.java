package sn.groupeisi.us.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.DomaineDto;
import sn.groupeisi.us.api.entity.DomaineEntity;
import sn.groupeisi.us.api.service.FiliereDomaineService;

import java.util.List;

@RestController
@RequestMapping("/domaine")
public class DomaineController {

    private final FiliereDomaineService filiereDomaineService;

    public DomaineController(FiliereDomaineService filiereDomaineService) {
        this.filiereDomaineService = filiereDomaineService;
    }

    // Ajouter un domaine à une filière (par ID)
    @PostMapping("/add/{filiereId}")
    public ResponseEntity<DomaineDto> addDomaine(@RequestBody String nom, @PathVariable Long filiereId) {
        DomaineDto domaine = filiereDomaineService.addDomaine(nom, filiereId);
        return ResponseEntity.ok(domaine);
    }

    // Ajouter un domaine à une filière (par nom de la filière)
    @PostMapping("/save")
    public ResponseEntity<DomaineDto> addDomaineToFiliereName(@RequestParam String nomDomaine, @RequestParam String nomFiliere) {
        DomaineDto domaine = filiereDomaineService.addDomaineToFiliere(nomDomaine, nomFiliere);
        return ResponseEntity.ok(domaine);
    }

    // Récupérer tous les domaines
    @GetMapping
    public ResponseEntity<List<DomaineDto>> findAllDomaines() {
        List<DomaineDto> domaines = filiereDomaineService.findAllDomaines();
        return ResponseEntity.ok(domaines);
    }

    // Récupérer un domaine par ID
    @GetMapping("/{id}")
    public ResponseEntity<DomaineDto> getDomaine(@PathVariable Long id) {
        DomaineDto domaine = filiereDomaineService.getDomaine(id);
        return ResponseEntity.ok(domaine);
    }

    // Récupérer tous les domaines d'une filière (par ID)
    @GetMapping("/filiere/{filiereId}")
    public ResponseEntity<List<DomaineDto>> getDomainesByFiliereId(@PathVariable Long filiereId) {
        List<DomaineDto> domaines = filiereDomaineService.getDomainesByFiliereId(filiereId);
        return ResponseEntity.ok(domaines);
    }

    // Supprimer un domaine par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomaine(@PathVariable Long id) {
        filiereDomaineService.deleteDomaine(id);
        return ResponseEntity.noContent().build();
    }











    // Ajouter un domaine à une filière (par ID)
    /*@PostMapping("/add/{filiereId}")
    public ResponseEntity<DomaineEntity> addDomaine(@RequestBody String nom, @PathVariable Long filiereId) {
        DomaineEntity domaine = filiereDomaineService.addDomaine(nom, filiereId);
        return ResponseEntity.ok(domaine);
    }

    // Ajouter un domaine à une filière (par nom de la filière)
    @PostMapping("/save")
    public ResponseEntity<DomaineEntity> addDomaineToFiliereName(@RequestParam String nomDomaine, @RequestParam String nomFiliere) {
        DomaineEntity domaine = filiereDomaineService.addDomaineToFiliereName(nomDomaine, nomFiliere);
        return ResponseEntity.ok(domaine);
    }

    // Récupérer tous les domaines
    @GetMapping
    public ResponseEntity<List<DomaineEntity>> findAllDomaines() {
        List<DomaineEntity> domaines = filiereDomaineService.findAllDomaines();
        return ResponseEntity.ok(domaines);
    }

    // Récupérer un domaine par ID
    @GetMapping("/{id}")
    public ResponseEntity<DomaineEntity> getDomaine(@PathVariable Long id) {
        DomaineEntity domaine = filiereDomaineService.getDomaine(id);
        return ResponseEntity.ok(domaine);
    }

    // Récupérer tous les domaines d'une filière (par ID)
    @GetMapping("/by-filiere/{filiereId}")
    public ResponseEntity<List<DomaineEntity>> getDomainesByFiliere(@PathVariable Long filiereId) {
        List<DomaineEntity> domaines = filiereDomaineService.getDomainesByFiliere(filiereId);
        return ResponseEntity.ok(domaines);
    }

    // Supprimer un domaine par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomaine(@PathVariable Long id) {
        filiereDomaineService.deleteDomaine(id);
        return ResponseEntity.noContent().build();
    }*/
}
