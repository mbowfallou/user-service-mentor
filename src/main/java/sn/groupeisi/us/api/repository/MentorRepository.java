package sn.groupeisi.us.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.groupeisi.us.api.entity.MentorEntity;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Long> {
    //List<MentorEntity> findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(String mentorName);

    @Query("SELECT DISTINCT m FROM MentorEntity m " +
            "LEFT JOIN m.disponibilite d " +
            "LEFT JOIN m.filieres f " +
            "LEFT JOIN m.domaines dm " +
            "WHERE (:nom IS NULL OR LOWER(CONCAT(m.prenom, ' ', m.nom)) LIKE LOWER(CONCAT('%', :nom, '%')) " +
            "   OR LOWER(m.prenom) LIKE LOWER(CONCAT('%', :nom, '%')) " +
            "   OR LOWER(m.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) " +
            "  AND (:statut IS NULL OR d.statut = :statut) " +
            "  AND (:filiere IS NULL OR f.nom = :filiere) " +
            "  AND (:domaine IS NULL OR dm.nom = :domaine)")
    List<MentorEntity> rechercherMentors(@Param("nom") String nom,
                                         @Param("statut") String statut,
                                         @Param("filiere") String filiere,
                                         @Param("domaine") String domaine);
}
