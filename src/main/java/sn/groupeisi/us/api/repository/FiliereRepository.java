package sn.groupeisi.us.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.groupeisi.us.api.entity.FiliereEntity;

import java.util.Optional;

@Repository
public interface FiliereRepository extends JpaRepository<FiliereEntity, Long> {
    Optional<FiliereEntity> findByNom(String nomFiliere);
    boolean existsByNom(String nomFiliere);
}
