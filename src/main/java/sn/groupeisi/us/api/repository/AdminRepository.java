package sn.groupeisi.us.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.groupeisi.us.api.entity.AdminEntity;
import sn.groupeisi.us.api.entity.EtudiantEntity;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
}
