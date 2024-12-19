package sn.groupeisi.us.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
//@EqualsAndHashCode(callSuper = true)
public class EtudiantEntity extends UserEntity{
    private String niveau;
    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private FiliereEntity filiere;
}
