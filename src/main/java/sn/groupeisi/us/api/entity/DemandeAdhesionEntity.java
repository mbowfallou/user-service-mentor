package sn.groupeisi.us.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "adhesions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeAdhesionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateDemande; // Date et heure de la demande

    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.EN_ATTENTE; // Exemple : "EN ATTENTE", "VALIDÉE", "REFUSÉE"

    private LocalDateTime dateTraitement; // Date et heure du traitement de la demande

    @OneToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    @JsonIgnore
    private MentorEntity mentor; // Référence au mentor qui a soumis la demande

    // Enum pour définir le statut de la user
    public enum StatutDemande {
        EN_ATTENTE, VALIDEE, REFUSEE,
    }
}
