package sn.groupeisi.us.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDto {
    private Long id;
    private String titre;
    private String description;
    private LocalDate debut;
    private LocalDate fin;
    private Long utilisateurId;
}
