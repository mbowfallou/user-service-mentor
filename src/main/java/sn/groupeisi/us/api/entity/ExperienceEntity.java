package sn.groupeisi.us.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity @Table(name = "experiences")
@Data @NoArgsConstructor @AllArgsConstructor
public class ExperienceEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String titre;
    @Column
    private String description;
    @Column
    private LocalDate debut;
    @Column
    private LocalDate fin;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
