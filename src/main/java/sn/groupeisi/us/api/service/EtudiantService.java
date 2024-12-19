package sn.groupeisi.us.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.entity.EtudiantEntity;
import sn.groupeisi.us.api.entity.RoleEntity;
import sn.groupeisi.us.api.repository.EtudiantRepository;
import sn.groupeisi.us.api.repository.RoleRepository;
import sn.groupeisi.us.api.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    // Enregistrer un étudiant
    public EtudiantEntity saveEtudiant(EtudiantEntity etudiant) {
        // Rechercher le rôle existant dans la base
        RoleEntity role = roleRepository.findByNom(etudiant.getRole().getNom())
                .orElseThrow(() -> new RuntimeException("Le rôle spécifié n'existe pas : " + etudiant.getRole().getNom()));

        // Associer le rôle trouvé à l'étudiant
        etudiant.setRole(role);

        // Sauvegarder l'étudiant
        return etudiantRepository.save(etudiant);
    }

    // Récupérer un étudiant par ID
    public Optional<EtudiantEntity> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }

    // Récupérer tous les étudiants
    public List<EtudiantEntity> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    // Récupérer tous les étudiants d'une filière
    public List<EtudiantEntity> getEtudiantsByFiliere(String filiere) {
        // Rechercher uniquement les utilisateurs de type étudiant avec une filière spécifique
        return userRepository.findAll().stream()
                .filter(user -> user instanceof EtudiantEntity)
                .map(user -> (EtudiantEntity) user)
                .filter(etudiant -> filiere.equals(etudiant.getFiliere()))
                .collect(Collectors.toList());
    }

    // Supprimer un étudiant
    public void deleteEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }
}
