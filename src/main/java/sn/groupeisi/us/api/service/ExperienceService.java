package sn.groupeisi.us.api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.dto.ExperienceDto;
import sn.groupeisi.us.api.entity.ExperienceEntity;
import sn.groupeisi.us.api.entity.UserEntity;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.mapper.ExperienceMapper;
import sn.groupeisi.us.api.repository.ExperienceRepository;
import sn.groupeisi.us.api.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final ExperienceMapper experienceMapper;

    public ExperienceService(ExperienceRepository experienceRepository,
                             UserRepository userRepository,
                             ExperienceMapper experienceMapper) {
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
        this.experienceMapper = experienceMapper;
    }

    @Transactional
    public ExperienceDto addExperienceToUser(ExperienceDto experienceDto, Long utilisateurId) {
        // Rechercher l'utilisateur existant par son ID
        UserEntity user = userRepository.findById(utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + utilisateurId));

        ExperienceEntity experience = experienceMapper.toEntity(experienceDto);
        experience.setUser(user);
        ExperienceEntity savedExperience = experienceRepository.save(experience);

        return experienceMapper.toDto(savedExperience);
    }

    public List<ExperienceDto> getExperiencesByUser(Long utilisateurId) {
        List<ExperienceEntity> experiences = experienceRepository.findByUserId(utilisateurId);

        if (experiences.isEmpty()) {
            throw new EntityNotFoundException("Aucune expérience trouvée pour l'utilisateur avec l'ID : " + utilisateurId);
        }

        return experiences.stream()
                .map(experienceMapper::toDto)
                .collect(Collectors.toList());
    }

    // Récupérer une expérience par ID
    public ExperienceDto getExperienceById(Long experienceId) {
        ExperienceEntity experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new EntityNotFoundException("Expérience introuvable avec l'ID : " + experienceId));
        return experienceMapper.toDto(experience);
    }

    // Supprimer une expérience par ID
    public void deleteExperience(Long experienceId) {
        ExperienceEntity experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new EntityNotFoundException("Expérience introuvable avec l'ID : " + experienceId));
        experienceRepository.delete(experience);
    }

    // Mettre à jour une expérience
    public ExperienceDto updateExperience(Long experienceId, ExperienceDto updatedExperienceDto) {
        ExperienceEntity existingExperience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new EntityNotFoundException("Expérience introuvable avec l'ID : " + experienceId));

        existingExperience.setTitre(updatedExperienceDto.getTitre());
        existingExperience.setDescription(updatedExperienceDto.getDescription());
        existingExperience.setDebut(updatedExperienceDto.getDebut());
        existingExperience.setFin(updatedExperienceDto.getFin());

        ExperienceEntity updatedExperience = experienceRepository.save(existingExperience);
        return experienceMapper.toDto(updatedExperience);
    }
}
