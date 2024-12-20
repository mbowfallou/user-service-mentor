package sn.groupeisi.us.api.service;

import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.entity.DemandeAdhesionEntity;
import sn.groupeisi.us.api.entity.MentorEntity;
import sn.groupeisi.us.api.entity.UserEntity;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.repository.DemandeAdhesionRepository;
import sn.groupeisi.us.api.repository.MentorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeAdhesionService {

    private final DemandeAdhesionRepository adhesionRepository;
    private final MentorRepository mentorRepository;

    public DemandeAdhesionService(DemandeAdhesionRepository demandeAdhesionRepository,
                                  MentorRepository mentorRepository) {
        this.adhesionRepository = demandeAdhesionRepository;
        this.mentorRepository = mentorRepository;
    }

    public DemandeAdhesionEntity getDemande(Long adhesionId){
        return adhesionRepository.findById(adhesionId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'ID " + adhesionId + " introuvable"));
    }

    public List<DemandeAdhesionEntity> getDemandeAdhesions() {
        return adhesionRepository.findAll();
        /*
        .stream()
                .map(demande -> demande.setMentor(new MentorEntity()))
                .collect(Collectors.toList())
         */
    }
    /**
     * Valider une demande d'adhésion.
     * Met le statut du mentor à "ACTIF" et la demande à "VALIDEE".
     */
    public DemandeAdhesionEntity validerDemande(Long demandeId) {
        DemandeAdhesionEntity demande = adhesionRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        if (!demande.getStatut().equals(DemandeAdhesionEntity.StatutDemande.EN_ATTENTE)) {
            throw new IllegalStateException("La demande est déjà traitée.");
        }

        // Modifier le statut de la demande
        demande.setStatut(DemandeAdhesionEntity.StatutDemande.VALIDEE);
        demande.setDateTraitement(LocalDateTime.now());

        // Activer le mentor
        MentorEntity mentor = demande.getMentor();
        mentor.setStatut(UserEntity.StatutUser.ACTIF);

        mentorRepository.save(mentor);
        return adhesionRepository.save(demande);
    }

    /**
     * Refuser une demande d'adhésion.
     * Laisse le mentor avec le statut "INACTIF" et met la demande à "REFUSEE".
     */
    public DemandeAdhesionEntity refuserDemande(Long demandeId) {
        DemandeAdhesionEntity demande = adhesionRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        if (!demande.getStatut().equals(DemandeAdhesionEntity.StatutDemande.EN_ATTENTE)) {
            throw new IllegalStateException("La demande est déjà traitée.");
        }

        // Modifier le statut de la demande
        demande.setStatut(DemandeAdhesionEntity.StatutDemande.REFUSEE);
        demande.setDateTraitement(LocalDateTime.now());

        return adhesionRepository.save(demande);
    }
}
