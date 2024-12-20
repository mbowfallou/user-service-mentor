package sn.groupeisi.us.api.service;

import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.dto.DisponibiliteDto;
import sn.groupeisi.us.api.entity.DisponibiliteEntity;
import sn.groupeisi.us.api.entity.MentorEntity;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.mapper.DisponibiliteMapper;
import sn.groupeisi.us.api.repository.DisponibiliteRepository;
import sn.groupeisi.us.api.repository.MentorRepository;

import java.util.List;

@Service
public class DisponibiliteService {
    private final DisponibiliteRepository disponibiliteRepository;
    private final MentorRepository mentorRepository;
    private final DisponibiliteMapper disponibiliteMapper;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository,
                                MentorRepository mentorRepository,
                                DisponibiliteMapper disponibiliteMapper) {
        this.disponibiliteRepository = disponibiliteRepository;
        this.mentorRepository = mentorRepository;
        this.disponibiliteMapper = disponibiliteMapper;
    }

    /**
     * Ajouter ou mettre à jour la disponibilité d'un mentor.
     *
     * @param mentorId           L'ID du mentor.
     * @param disponibiliteDTO   Les informations de disponibilité.
     * @return La disponibilité ajoutée ou mise à jour (DTO).
     */
    public DisponibiliteDto ajouterOuMettreAJourDisponibilite(Long mentorId, DisponibiliteDto disponibiliteDTO) {
        MentorEntity mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Mentor introuvable avec l'ID : " + mentorId));

        DisponibiliteEntity disponibilite = mentor.getDisponibilite();
        DisponibiliteEntity updatedDisponibilite;

        if (disponibilite == null) {
            // Créer une nouvelle disponibilité
            updatedDisponibilite = disponibiliteMapper.toEntity(disponibiliteDTO);
            updatedDisponibilite.setMentor(mentor);
            mentor.setDisponibilite(updatedDisponibilite);
        } else {
            // Mettre à jour la disponibilité existante
            disponibilite.setStatut(disponibiliteDTO.getStatut());
            disponibilite.setJours(disponibiliteDTO.getJours());
            disponibilite.setHeures(disponibiliteDTO.getHeures());
            disponibilite.setPeriode(disponibiliteDTO.getPeriode());
            updatedDisponibilite = disponibilite;
        }

        mentorRepository.save(mentor); // Cascade.ALL gère la sauvegarde
        return disponibiliteMapper.toDto(updatedDisponibilite);
    }

    /**
     * Supprimer la disponibilité d'un mentor.
     *
     * @param mentorId L'ID du mentor.
     */
    public void supprimerDisponibilite(Long mentorId) {
        MentorEntity mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Mentor introuvable avec l'ID : " + mentorId));

        DisponibiliteEntity disponibilite = mentor.getDisponibilite();
        if (disponibilite == null) {
            throw new EntityNotFoundException("Aucune disponibilité associée au mentor avec l'ID : " + mentorId);
        }

        mentor.setDisponibilite(null); // OrphanRemoval supprime automatiquement
        mentorRepository.save(mentor);
    }

    /**
     * Récupérer la disponibilité d'un mentor.
     *
     * @param mentorId L'ID du mentor.
     * @return La disponibilité associée au mentor (DTO).
     */
    public DisponibiliteDto getDisponibiliteByMentor(Long mentorId) {
        MentorEntity mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Mentor introuvable avec l'ID : " + mentorId));

        DisponibiliteEntity disponibilite = mentor.getDisponibilite();
        if (disponibilite == null) {
            throw new EntityNotFoundException("Aucune disponibilité associée au mentor avec l'ID : " + mentorId);
        }

        return disponibiliteMapper.toDto(disponibilite);
    }
}
