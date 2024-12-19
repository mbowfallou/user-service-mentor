package sn.groupeisi.us.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.entity.EtudiantEntity;
import sn.groupeisi.us.api.service.EtudiantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/etudiant")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    // Enregistrer un étudiant
    @PostMapping("/save")
    public ResponseEntity<EtudiantEntity> saveEtudiant(@RequestBody EtudiantEntity etudiant) {
        EtudiantEntity savedEtudiant = etudiantService.saveEtudiant(etudiant);
        return ResponseEntity.ok(savedEtudiant);
    }

    // Récupérer un étudiant par ID
    @GetMapping("/{id}")
    public ResponseEntity<EtudiantEntity> getEtudiantById(@PathVariable Long id) {
        Optional<EtudiantEntity> etudiant = etudiantService.getEtudiantById(id);
        if (etudiant.isPresent()) {
            return ResponseEntity.ok(etudiant.get());
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // Récupérer tous les étudiants
    @GetMapping("/all")
    public ResponseEntity<List<EtudiantEntity>> getAllEtudiants() {
        List<EtudiantEntity> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/filiere/{filiere}")
    public ResponseEntity<List<EtudiantEntity>> getEtudiantsByFiliere(@PathVariable String filiere) {
        List<EtudiantEntity> etudiants = etudiantService.getEtudiantsByFiliere(filiere);
        return ResponseEntity.ok(etudiants);
    }

    // Supprimer un étudiant
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.ok("Étudiant avec l'ID " + id + " supprimé.");
    }
}
