package sn.groupeisi.us.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity @Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String prenom;
    @Column
    private String nom;
    @Column
    private String adresse;
    @Column
    private LocalDate dateNaissance;
    @Column
    private String email;
    @Column
    private String nomUtilisateur;
    @Column
    private String motDePasse;
    @Column
    private String photo;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Enumerated(EnumType.STRING)
    private StatutUser statut = StatutUser.INACTIF;

    // Enum pour définir le statut de la user
    public enum StatutUser {
        ACTIF, INACTIF, BLOQUE,
    }
}
