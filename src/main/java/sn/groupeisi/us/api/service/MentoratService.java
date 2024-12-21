package sn.groupeisi.us.api.service;

import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.dto.DemandeDeMentoratDto;
import sn.groupeisi.us.api.entity.DemandeDeMentoratEntity;
import sn.groupeisi.us.api.entity.DomaineEntity;
import sn.groupeisi.us.api.entity.EtudiantEntity;
import sn.groupeisi.us.api.entity.MentorEntity;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.mapper.DemandeDeMentoratMapper;
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
    private final DemandeDeMentoratMapper demandeDeMentoratMapper;

    public MentoratService(
            DemandeDeMentoratRepository demandeDeMentoratRepository,
            EtudiantRepository etudiantRepository,
            MentorRepository mentorRepository,
            DomaineRepository domaineRepository,
            DemandeDeMentoratMapper demandeDeMentoratMapper
    ) {
        this.demandeDeMentoratRepository = demandeDeMentoratRepository;
        this.etudiantRepository = etudiantRepository;
        this.mentorRepository = mentorRepository;
        this.domaineRepository = domaineRepository;
        this.demandeDeMentoratMapper = demandeDeMentoratMapper;
    }

    /**
     * Envoyer une demande de mentorat.
     */
    public DemandeDeMentoratDto envoyerDemande(Long etudiantId, Long mentorId, Set<Long> domaineIds) {
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

        // Exemple de retour du DTO après création
        DemandeDeMentoratEntity savedDemande = demandeDeMentoratRepository.save(demande);
        return demandeDeMentoratMapper.toDto(savedDemande);
    }

    /**
     * Accepter une demande de mentorat.
     */
    public DemandeDeMentoratDto accepterDemande(Long demandeId) {
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        if (demande.getStatut() != DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Cette demande a déjà été traitée.");
        }

        demande.setStatut(DemandeDeMentoratEntity.StatutDemande.ACCEPTEE);
        demande.setDateTraitement(LocalDateTime.now());

        return demandeDeMentoratMapper.toDto(demandeDeMentoratRepository.save(demande));
    }

    /**
     * Refuser une demande de mentorat.
     */
    public DemandeDeMentoratDto refuserDemande(Long demandeId) {
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        if (demande.getStatut() != DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Cette demande a déjà été traitée.");
        }

        demande.setStatut(DemandeDeMentoratEntity.StatutDemande.REFUSEE);
        demande.setDateTraitement(LocalDateTime.now());

        return demandeDeMentoratMapper.toDto(demandeDeMentoratRepository.save(demande));
    }


    /**
     * Annuler une demande de mentorat par un étudiant.
     */
    public DemandeDeMentoratDto annulerDemande(Long demandeId, Long etudiantId) {
        // Récupérer la demande
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        // Vérifier si l'étudiant est bien l'auteur de la demande
        if (!demande.getEtudiant().getId().equals(etudiantId)) {
            throw new IllegalArgumentException("Vous ne pouvez pas annuler une demande qui ne vous appartient pas.");
        }

        // Vérifier si la demande est déjà traitée
        if (demande.getStatut() != DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Vous ne pouvez pas annuler une demande déjà traitée.");
        }

        // Mettre à jour le statut de la demande
        demande.setStatut(DemandeDeMentoratEntity.StatutDemande.ANNULEE);
        demande.setDateTraitement(LocalDateTime.now());

        // Sauvegarder et retourner le DTO
        DemandeDeMentoratEntity updatedDemande = demandeDeMentoratRepository.save(demande);
        return demandeDeMentoratMapper.toDto(updatedDemande);
    }


    /**
     * Supprimer une demande de mentorat par un étudiant.
     */
    public void supprimerDemande(Long demandeId, Long etudiantId) {
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));

        if (!demande.getEtudiant().getId().equals(etudiantId)) {
            throw new IllegalArgumentException("Vous ne pouvez pas supprimer une demande qui ne vous appartient pas.");
        }

        if (demande.getStatut() != DemandeDeMentoratEntity.StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Vous ne pouvez pas supprimer une demande déjà traitée.");
        }

        demandeDeMentoratRepository.delete(demande);
    }

    /**
     * Obtenir toutes les demandes de mentorat.
     */
    public List<DemandeDeMentoratDto> getAllDemandes() {
        List<DemandeDeMentoratEntity> demandes = demandeDeMentoratRepository.findAll();
        return demandes.stream().map(demandeDeMentoratMapper::toDto).toList();
    }


    /**
     * Obtenir toutes les demandes d'un mentor.
     */
    public List<DemandeDeMentoratDto> getDemandesByMentor(Long mentorId) {
        List<DemandeDeMentoratEntity> demandes = demandeDeMentoratRepository.findByMentorId(mentorId);
        return demandes.stream().map(demandeDeMentoratMapper::toDto).toList();
    }

    /**
     * Obtenir toutes les demandes d'un étudiant.
     */
    public List<DemandeDeMentoratDto> getDemandesByEtudiant(Long etudiantId) {
        List<DemandeDeMentoratEntity> demandes = demandeDeMentoratRepository.findByEtudiantId(etudiantId);
        return demandes.stream().map(demandeDeMentoratMapper::toDto).toList();
    }

    /**
     * Obtenir une demande spécifique par ID.
     */
    public DemandeDeMentoratDto getDemandeById(Long demandeId) {
        DemandeDeMentoratEntity demande = demandeDeMentoratRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + demandeId));
        return demandeDeMentoratMapper.toDto(demande);
    }

}
