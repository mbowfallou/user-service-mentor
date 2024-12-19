package sn.groupeisi.us.api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "filieres")
public class FiliereEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom; // Exemple : Génie Logiciel, Big Data

    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL)
    private List<DomaineEntity> domaines = new ArrayList<>();
}
