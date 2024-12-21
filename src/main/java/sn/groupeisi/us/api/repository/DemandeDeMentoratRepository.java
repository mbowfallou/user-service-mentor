package sn.groupeisi.us.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.groupeisi.us.api.entity.DemandeDeMentoratEntity;

import java.util.List;

@Repository
public interface DemandeDeMentoratRepository extends JpaRepository<DemandeDeMentoratEntity, Long> {
    List<DemandeDeMentoratEntity> findByMentorId(Long mentorId);

    List<DemandeDeMentoratEntity> findByEtudiantId(Long etudiantId);
}
