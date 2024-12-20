package sn.groupeisi.us.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DisponibiliteDto {
    private Long id;
    @NotBlank(message = "Le statut est obligatoire.")
    private String statut;
    private String jours;
    private String heures;
    private String periode;
    private Long mentorId;
}
