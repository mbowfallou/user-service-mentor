package sn.groupeisi.us.api.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminEntity extends UserEntity{
    private String permissions;
}
