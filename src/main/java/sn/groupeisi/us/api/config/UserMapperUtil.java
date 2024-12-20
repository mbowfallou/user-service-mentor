package sn.groupeisi.us.api.config;

import org.springframework.stereotype.Component;
import sn.groupeisi.us.api.dto.AdminDto;
import sn.groupeisi.us.api.dto.EtudiantDto;
import sn.groupeisi.us.api.dto.MentorDto;
import sn.groupeisi.us.api.dto.UserDto;
import sn.groupeisi.us.api.entity.*;

import java.util.stream.Collectors;

@Component
public class UserMapperUtil {

    public static <T extends UserDto> T mapToDto(UserEntity entity, Class<T> dtoClass) {
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();

            // Mapping des champs communs
            dto.setId(entity.getId());
            dto.setPrenom(entity.getPrenom());
            dto.setNom(entity.getNom());
            dto.setAdresse(entity.getAdresse());
            dto.setDateNaissance(entity.getDateNaissance());
            dto.setEmail(entity.getEmail());
            dto.setNomUtilisateur(entity.getNomUtilisateur());
            dto.setRoleNom(entity.getRole().getNom());
            dto.setStatut(entity.getStatut() != null ? entity.getStatut().toString() : "INDEFINI");

            // Mapping spécifique selon le type
            if (dto instanceof EtudiantDto && entity instanceof EtudiantEntity) {
                mapEtudiantSpecificFields((EtudiantEntity) entity, (EtudiantDto) dto);
            } else if (dto instanceof MentorDto && entity instanceof MentorEntity) {
                mapMentorSpecificFields((MentorEntity) entity, (MentorDto) dto);
            } else if (dto instanceof AdminDto && entity instanceof AdminEntity) {
                mapAdminSpecificFields((AdminEntity) entity, (AdminDto) dto);
            }

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du mapping entity-dto", e);
        }
    }

    private static void mapEtudiantSpecificFields(EtudiantEntity entity, EtudiantDto dto) {
        dto.setFiliereId(entity.getFiliere().getId());
        dto.setNiveau(entity.getNiveau());
    }

    private static void mapMentorSpecificFields(MentorEntity entity, MentorDto dto) {
        dto.setFiliereIds(entity.getFilieres().stream()
                .map(FiliereEntity::getId)
                .collect(Collectors.toSet()));
        dto.setDomaineIds(entity.getDomaines().stream()
                .map(DomaineEntity::getId)
                .collect(Collectors.toSet()));
        dto.setDescription(entity.getDescription());
    }

    private static void mapAdminSpecificFields(AdminEntity entity, AdminDto dto) {
        dto.setPermissions(entity.getPermissions());
    }
}
