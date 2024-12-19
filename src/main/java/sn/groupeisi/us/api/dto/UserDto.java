package sn.groupeisi.us.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private String prenom;
    private String nom;
    private String adresse;
    private LocalDate dateNaissance;
    private String email;
    private String nomUtilisateur;
    private String motDePasse;
    private String photo;
}
