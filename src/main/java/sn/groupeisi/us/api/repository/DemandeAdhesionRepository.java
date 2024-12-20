package sn.groupeisi.us.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.groupeisi.us.api.entity.DemandeAdhesionEntity;

@Repository
public interface DemandeAdhesionRepository extends JpaRepository<DemandeAdhesionEntity, Long> {
}
