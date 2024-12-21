package sn.groupeisi.us.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "demande_mentorat")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeDeMentoratEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private EtudiantEntity etudiant; // L'étudiant qui envoie la demande

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private MentorEntity mentor; // Le mentor visé par la demande

    @ManyToMany
    @JoinTable(
            name = "demande_mentorat_domaines",
            joinColumns = @JoinColumn(name = "demande_id"),
            inverseJoinColumns = @JoinColumn(name = "domaine_id")
    )
    private Set<DomaineEntity> domaines; // Les domaines spécifiques de la demande

    @Column(nullable = false)
    private LocalDateTime dateDemande; // Date d'envoi de la demande

    @Column
    private LocalDateTime dateTraitement; // Date d'envoi de la demande

    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.EN_ATTENTE; // Statut initial

    public enum StatutDemande {
        EN_ATTENTE, ACCEPTEE, REFUSEE
    }
}
