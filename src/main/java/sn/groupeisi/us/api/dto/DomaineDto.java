package sn.groupeisi.us.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomaineDto {
    private Long id;
    private String nom;
    private String filiereNom;
}
