package sn.groupeisi.us.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disponibilites")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisponibiliteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String statut; // ouvert, plein, ou indisponible
    private String jours;  // Exemple : "lundi, jeudi"
    private String heures; // Exemple : "18h-20h"
    private String periode; // Exemple : "février à juin"

    @OneToOne(mappedBy = "disponibilite", cascade = CascadeType.ALL, orphanRemoval = true)
    private MentorEntity mentor;
}
