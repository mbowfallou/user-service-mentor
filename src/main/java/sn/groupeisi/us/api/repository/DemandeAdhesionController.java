package sn.groupeisi.us.api.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.entity.DemandeAdhesionEntity;
import sn.groupeisi.us.api.service.DemandeAdhesionService;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
public class DemandeAdhesionController {

    private final DemandeAdhesionService demandeAdhesionService;

    public DemandeAdhesionController(DemandeAdhesionService demandeAdhesionService) {
        this.demandeAdhesionService = demandeAdhesionService;
    }

    @GetMapping
    public ResponseEntity<List<DemandeAdhesionEntity>> getRequests(){
        return ResponseEntity.ok(demandeAdhesionService.getDemandeAdhesions());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DemandeAdhesionEntity> getDemande(@PathVariable Long id){
        return ResponseEntity.ok(demandeAdhesionService.getDemande(id));
    }

    /**
     * Endpoint pour valider une demande d'adhésion.
     */
    @PutMapping("/{demandeId}/valider")
    public ResponseEntity<DemandeAdhesionEntity> validerDemande(@PathVariable Long demandeId) {
        DemandeAdhesionEntity demande = demandeAdhesionService.validerDemande(demandeId);
        return ResponseEntity.ok(demande);
    }

    /**
     * Endpoint pour refuser une demande d'adhésion.
     */
    @PutMapping("/{demandeId}/refuser")
    public ResponseEntity<DemandeAdhesionEntity> refuserDemande(@PathVariable Long demandeId) {
        DemandeAdhesionEntity demande = demandeAdhesionService.refuserDemande(demandeId);
        return ResponseEntity.ok(demande);
    }
}
