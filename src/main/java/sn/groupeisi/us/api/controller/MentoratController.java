package sn.groupeisi.us.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.DemandeDeMentoratRequest;
import sn.groupeisi.us.api.entity.DemandeDeMentoratEntity;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.service.MentoratService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/mentorat")
public class MentoratController {
    private final MentoratService mentoratService;

    public MentoratController(MentoratService mentoratService) {
        this.mentoratService = mentoratService;
    }

    @PostMapping("/demandes")
    public ResponseEntity<DemandeDeMentoratEntity> envoyerDemande(
            @RequestBody DemandeDeMentoratRequest demandeRequest
    ) {
        DemandeDeMentoratEntity demande = mentoratService.envoyerDemande(
                demandeRequest.getEtudiantId(),
                demandeRequest.getMentorId(),
                new HashSet<>(demandeRequest.getDomaines())
        );
        return ResponseEntity.ok(demande);
    }

    @PutMapping("/demandes/{id}/accepter")
    public ResponseEntity<DemandeDeMentoratEntity> accepterDemande(@PathVariable Long id) {
        try {
            DemandeDeMentoratEntity demande = mentoratService.accepterDemande(id);
            return ResponseEntity.ok(demande);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PutMapping("/demandes/{id}/refuser")
    public ResponseEntity<DemandeDeMentoratEntity> refuserDemande(@PathVariable Long id) {
        try {
            DemandeDeMentoratEntity demande = mentoratService.refuserDemande(id);
            return ResponseEntity.ok(demande);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/mentor/{mentorId}/demandes")
    public ResponseEntity<List<DemandeDeMentoratEntity>> getDemandesByMentor(@PathVariable Long mentorId) {
        return ResponseEntity.ok(mentoratService.getDemandesByMentor(mentorId));
    }

    @GetMapping("/etudiant/{etudiantId}/demandes")
    public ResponseEntity<List<DemandeDeMentoratEntity>> getDemandesByEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(mentoratService.getDemandesByEtudiant(etudiantId));
    }

    @DeleteMapping("/demandes/{id}/annuler")
    public ResponseEntity<String> annulerDemande(@PathVariable Long id, @RequestParam Long etudiantId) {
        try {
            mentoratService.annulerDemande(id, etudiantId);
            return ResponseEntity.ok("Demande de mentorat annulée avec succès.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
