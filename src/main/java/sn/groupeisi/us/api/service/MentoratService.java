package sn.groupeisi.us.api.service;

import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.entity.DemandeDeMentoratEntity;
import sn.groupeisi.us.api.entity.DomaineEntity;
import sn.groupeisi.us.api.entity.EtudiantEntity;
import sn.groupeisi.us.api.entity.MentorEntity;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.repository.DemandeDeMentoratRepository;
import sn.groupeisi.us.api.repository.DomaineRepository;
import sn.groupeisi.us.api.repository.EtudiantRepository;
import sn.groupeisi.us.api.repository.MentorRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MentoratService {

    private final DemandeDeMentoratRepository demandeDeMentoratRepository;
    private final EtudiantRepository etudiantRepository;
    private final MentorRepository mentorRepository;
    private final DomaineRepository domaineRepository;

    public MentoratService(
            DemandeDeMentoratRepository demandeDeMentoratRepository,
            EtudiantRepository etudiantRepository,
            MentorRepository mentorRepository,
            DomaineRepository domaineRepository
    ) {
        this.demandeDeMentoratRepository = demandeDeMentoratRepository;
        this.etudiantRepository = etudiantRepository;
        this.mentorRepository = mentorRepository;
        this.domaineRepository = domaineRepository;
    }

    /**
     * Envoyer une demande de mentorat.
     */
    public DemandeDeMentoratEntity envoyerDemande(Long etudiantId, Long mentorId, Set<Long> domaineIds) {
        EtudiantEntity etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new EntityNotFoundException("Étudiant introuvable avec l'ID : " + etudiantId));

        MentorEntity mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Mentor introuvable avec l'ID : " + mentorId));

        Set<DomaineEntity> domaines = new HashSet<>(domaineRepository.findAllById(domaineIds));
        if (domaines.isEmpty()) {
            throw new IllegalArgumentException("Domaines invalides ou non spécifiés");
        }

        DemandeDeMentoratEntity demande = new DemandeDeMentoratEntity();
        demande.setEtudiant(etudiant);
        demande.setMentor(mentor);
        demande.setDomaines(domaines);
        demande.setDateDemande(LocalDateTime.now());
        demande.setStatut(DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE);

        return demandeDeMentoratRepository.save(demande);
    }

    /**
     * Accepter une demande de mentorat.
     */
    public DemandeDeMentoratEntity accepterDemande(Long demandeId) {
        // Récupérer la demande par ID
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        // Vérifier si la demande a déjà été traitée
        if (demande.getStatut() != DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Cette demande a déjà été traitée avec le statut : " + demande.getStatut());
        }

        // Mettre à jour le statut de la demande
        demande.setStatut(DemandeDeMentoratEntity.StatutDemande.ACCEPTEE);
        demande.setDateTraitement(LocalDateTime.now()); // Mettre à jour la date de traitement

        return demandeDeMentoratRepository.save(demande);
    }


    /**
     * Refuser une demande de mentorat.
     */
    public DemandeDeMentoratEntity refuserDemande(Long demandeId) {
        // Récupérer la demande par ID
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        // Vérifier si la demande a déjà été traitée
        if (demande.getStatut() != DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Cette demande a déjà été traitée avec le statut : " + demande.getStatut());
        }

        // Mettre à jour le statut de la demande
        demande.setStatut(DemandeDeMentoratEntity.StatutDemande.REFUSEE);
        demande.setDateTraitement(LocalDateTime.now()); // Mettre à jour la date de traitement

        return demandeDeMentoratRepository.save(demande);
    }


    /**
     * Lister les demandes pour un mentor.
     */
    public List<DemandeDeMentoratEntity> getDemandesByMentor(Long mentorId) {
        return demandeDeMentoratRepository.findByMentorId(mentorId);
    }

    /**
     * Lister les demandes pour un étudiant.
     */
    public List<DemandeDeMentoratEntity> getDemandesByEtudiant(Long etudiantId) {
        return demandeDeMentoratRepository.findByEtudiantId(etudiantId);
    }

    public DemandeDeMentoratEntity annulerDemande(Long demandeId, Long etudiantId) {
        // Récupérer la demande
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        // Vérifier si l'étudiant est bien l'auteur de la demande
        if (!demande.getEtudiant().getId().equals(etudiantId)) {
            throw new IllegalStateException("Vous ne pouvez pas annuler une demande qui ne vous appartient pas.");
        }

        // Vérifier si la demande a déjà été traitée
        if (demande.getStatut() != DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Vous ne pouvez pas annuler une demande qui a déjà été traitée.");
        }

        // Supprimer la demande
        demandeDeMentoratRepository.delete(demande);

        return demande;
    }

}
