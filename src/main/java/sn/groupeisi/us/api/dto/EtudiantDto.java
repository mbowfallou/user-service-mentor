package sn.groupeisi.us.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EtudiantDto extends UserDto {
    private String niveau;
    private Long filiereId; // ID de la filière choisie
}
