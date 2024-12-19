package sn.groupeisi.us.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.ExperienceDto;
import sn.groupeisi.us.api.entity.ExperienceEntity;
import sn.groupeisi.us.api.service.ExperienceService;

import java.util.List;

@RestController
@RequestMapping("/exp")
public class ExperienceController {
    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    // Ajouter une expérience pour un utilisateur
    @PostMapping("/user/{userId}")
    public ResponseEntity<ExperienceDto> addExperienceToUser(@RequestBody ExperienceDto experienceDto, @PathVariable Long userId) {
        ExperienceDto savedExperience = experienceService.addExperienceToUser(experienceDto, userId);
        return ResponseEntity.ok(savedExperience);
    }

    // Récupérer les expériences d’un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExperienceDto>> getExperiencesByUser(@PathVariable Long userId) {
        List<ExperienceDto> experiences = experienceService.getExperiencesByUser(userId);
        return ResponseEntity.ok(experiences);
    }

    // Récupérer une expérience par ID
    @GetMapping("/{id}")
    public ResponseEntity<ExperienceDto> getExperienceById(@PathVariable Long id) {
        ExperienceDto experience = experienceService.getExperienceById(id);
        return ResponseEntity.ok(experience);
    }

    // Supprimer une expérience par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour une expérience
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceDto> updateExperience(@PathVariable Long id, @RequestBody ExperienceDto experienceDto) {
        ExperienceDto updatedExperience = experienceService.updateExperience(id, experienceDto);
        return ResponseEntity.ok(updatedExperience);
    }
}
