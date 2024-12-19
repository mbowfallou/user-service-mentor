package sn.groupeisi.us.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.groupeisi.us.api.entity.AdminEntity;
import sn.groupeisi.us.api.entity.EtudiantEntity;

import java.util.List;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
}
