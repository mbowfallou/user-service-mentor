package sn.groupeisi.us.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.groupeisi.us.api.entity.DomaineEntity;
import sn.groupeisi.us.api.entity.FiliereEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomaineRepository extends JpaRepository<DomaineEntity, Long> {
    Optional<DomaineEntity> findByNomAndFiliere(String nom, FiliereEntity filiere);
    List<DomaineEntity> findByNom(String nomDomaine);
    List<DomaineEntity> findByFiliere(FiliereEntity filiere);
    boolean existsByNom(String nomDomaine);
    List<DomaineEntity> findByFiliere_Id(Long filiereId);
}

