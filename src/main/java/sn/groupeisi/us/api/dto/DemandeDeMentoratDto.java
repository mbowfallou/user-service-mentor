package sn.groupeisi.us.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeDeMentoratDto {
    private Long id;
    private Long etudiantId; // ID de l'étudiant
    private String etudiantNom; // Nom complet de l'étudiant
    private Long mentorId; // ID du mentor
    private String mentorNom; // Nom complet du mentor
    private Set<Long> domaineIds; // IDs des domaines
    private Set<String> domaineNoms; // Noms des domaines
    private LocalDateTime dateDemande; // Date d'envoi de la demande
    private LocalDateTime dateTraitement; // Date de traitement de la demande (s'il y en a)
    private String statut; // Statut de la demande (EN_ATTENTE, ACCEPTEE, REFUSEE)
}
