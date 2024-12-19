package sn.groupeisi.us.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String prenom;
    private String nom;
    private String adresse;
    private LocalDate dateNaissance;
    private String email;
    private String nomUtilisateur;
    private String motDePasse;
    private String photo;
    private String roleNom;
    private String statut;
}
