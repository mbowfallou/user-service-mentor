package sn.groupeisi.us.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
//@EqualsAndHashCode(callSuper = true)
public class MentorEntity extends UserEntity{
    private String specialite;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "mentor_filieres",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "filiere_id")
    )
    private Set<FiliereEntity> filieres;

    @ManyToMany
    @JoinTable(
            name = "mentor_domaines",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "domaine_id")
    )
    private Set<DomaineEntity> domaines;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "disponibilite_id", referencedColumnName = "id")
    private DisponibiliteEntity disponibilite; // Disponibilité du mentor

    @OneToOne(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)

    private DemandeAdhesionEntity demandeAdhesion; // Demande associée au mentor
}
