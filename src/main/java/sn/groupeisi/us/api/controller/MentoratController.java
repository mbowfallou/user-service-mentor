package sn.groupeisi.us.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.DemandeDeMentoratDto;
import sn.groupeisi.us.api.dto.DemandeDeMentoratRequest;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.service.MentoratService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mentorat")
public class MentoratController {
    private final MentoratService mentoratService;

    public MentoratController(MentoratService mentoratService) {
        this.mentoratService = mentoratService;
    }

    /**
     * Envoyer une demande de mentorat.
     */
    @PostMapping("/demande")
    public ResponseEntity<DemandeDeMentoratDto> envoyerDemande(
            @RequestBody DemandeDeMentoratRequest demandeRequest
    ) {
        DemandeDeMentoratDto demande = mentoratService.envoyerDemande(
                demandeRequest.getEtudiantId(),
                demandeRequest.getMentorId(),
                new HashSet<>(demandeRequest.getDomaines())
        );
        return ResponseEntity.ok(demande);
    }

    /**
     * Accepter une demande de mentorat.
     */
    @PutMapping("/demande/{id}/accepter")
    public ResponseEntity<DemandeDeMentoratDto> accepterDemande(@PathVariable Long id) {
        try {
            DemandeDeMentoratDto demande = mentoratService.accepterDemande(id);
            return ResponseEntity.ok(demande);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Refuser une demande de mentorat.
     */
    @PutMapping("/demande/{id}/refuser")
    public ResponseEntity<DemandeDeMentoratDto> refuserDemande(@PathVariable Long id) {
        try {
            DemandeDeMentoratDto demande = mentoratService.refuserDemande(id);
            return ResponseEntity.ok(demande);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtenir toutes les demandes de mentorat.
     */
    @GetMapping("/demandes")
    public ResponseEntity<List<DemandeDeMentoratDto>> getAllDemandes() {
        List<DemandeDeMentoratDto> demandes = mentoratService.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }


    /**
     * Obtenir les demandes de mentorat pour un mentor.
     */
    @GetMapping("/mentor/{mentorId}/demandes")
    public ResponseEntity<List<DemandeDeMentoratDto>> getDemandesByMentor(@PathVariable Long mentorId) {
        List<DemandeDeMentoratDto> demandes = mentoratService.getDemandesByMentor(mentorId);
        return ResponseEntity.ok(demandes);
    }

    /**
     * Obtenir les demandes de mentorat pour un étudiant.
     */
    @GetMapping("/etudiant/{etudiantId}/demandes")
    public ResponseEntity<List<DemandeDeMentoratDto>> getDemandesByEtudiant(@PathVariable Long etudiantId) {
        List<DemandeDeMentoratDto> demandes = mentoratService.getDemandesByEtudiant(etudiantId);
        return ResponseEntity.ok(demandes);
    }

    /**
     * Annuler une demande de mentorat par un étudiant.
     */
    @DeleteMapping("/annuler")
    public ResponseEntity<String> annulerDemande(@RequestBody Map<String, Long> payload) {
        Long demandeId = payload.get("demandeId");
        Long etudiantId = payload.get("etudiantId");

        // Vérifie si les deux paramètres sont présents
        if (demandeId == null || etudiantId == null) {
            return ResponseEntity.badRequest().body("Les champs 'demandeId' et 'etudiantId' sont obligatoires.");
        }

        try {
            mentoratService.annulerDemande(demandeId, etudiantId);
            return ResponseEntity.ok("Demande de mentorat annulée avec succès.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Obtenir une demande spécifique par ID.
     */
    @GetMapping("/demande/{id}")
    public ResponseEntity<DemandeDeMentoratDto> getDemandeById(@PathVariable Long id) {
        DemandeDeMentoratDto demande = mentoratService.getDemandeById(id);
        return ResponseEntity.ok(demande);
    }

}
