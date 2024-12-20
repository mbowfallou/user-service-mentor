package sn.groupeisi.us.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class MentorDto extends UserDto{
    private String specialite;
    private String description;
    private Set<Long> filiereIds;  // Liste des IDs des filières
    private Set<Long> domaineIds;  // Liste des IDs des domaines
    private DisponibiliteDto disponibilite;
}
