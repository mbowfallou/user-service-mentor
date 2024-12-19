package sn.groupeisi.us.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "domaines")
public class DomaineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom; // Exemple : DevOps, Développement Mobile, Web

    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private FiliereEntity filiere;
}
