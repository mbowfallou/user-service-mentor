package sn.groupeisi.us.api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.config.PasswordUtil;
import sn.groupeisi.us.api.dto.EtudiantDto;
import sn.groupeisi.us.api.dto.MentorDto;
import sn.groupeisi.us.api.dto.UserDto;
import sn.groupeisi.us.api.entity.*;
import sn.groupeisi.us.api.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private DomaineRepository domaineRepository;

    //String hashedPassword = PasswordUtil.encode("monMotDePasse");
    //boolean matches = PasswordUtil.matches("monMotDePasse", hashedPassword);


    public ResponseEntity<?> registerUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()) || userRepository.existsByNomUtilisateur(userDto.getNomUtilisateur())) {
            throw new IllegalArgumentException("Cet email ou username est déjà utilisé.");
        }

        if (userDto instanceof EtudiantDto etudiantDto) {
            return registerEtudiant(etudiantDto);
        } else if (userDto instanceof MentorDto mentorDto) {
            return registerMentor(mentorDto);
        } else {
            return ResponseEntity.badRequest().body("Type d'utilisateur non pris en charge.");
        }
    }

    private ResponseEntity<?> registerEtudiant(EtudiantDto etudiantDto) {
        // Vérifier la filière
        FiliereEntity filiere = filiereRepository.findById(etudiantDto.getFiliereId())
                .orElseThrow(() -> new IllegalArgumentException("Filière introuvable"));

        // Récupérer le rôle 'ETUDIANT'
        RoleEntity roleEtudiant = roleRepository.findByNom("ETUDIANT")
                .orElseThrow(() -> new IllegalArgumentException("Role ETUDIANT introuvable"));


        // Créer l'entité Etudiant
        EtudiantEntity etudiant = new EtudiantEntity();
        copyUserDetails(etudiantDto, etudiant); // Méthode générique pour copier les champs
        etudiant.setFiliere(filiere);
        etudiant.setNiveau(etudiantDto.getNiveau());
        etudiant.setRole(roleEtudiant); // Affecter le rôle
        etudiantRepository.save(etudiant);

        return ResponseEntity.ok("Étudiant inscrit avec succès");
    }

    private ResponseEntity<?> registerMentor(MentorDto mentorDto) {
        // Vérifier les filières et //domaines
        Set<FiliereEntity> filieres = new HashSet<>(filiereRepository.findAllById(mentorDto.getFiliereIds()));
        Set<DomaineEntity> domaines = new HashSet<>(domaineRepository.findAllById(mentorDto.getDomaineIds()));

        // Récupérer le rôle 'MENTOR'
        RoleEntity roleMentor = roleRepository.findByNom("MENTOR")
                .orElseThrow(() -> new IllegalArgumentException("Role MENTOR introuvable"));

        if (filieres.isEmpty()) // || domaines.isEmpty()
        {
            throw new IllegalArgumentException("Filières invalides");
        }

        // Créer l'entité Mentor
        MentorEntity mentor = new MentorEntity();
        copyUserDetails(mentorDto, mentor);
        mentor.setDescription(mentorDto.getDescription());
        mentor.setFilieres(filieres);
        mentor.setDomaines(domaines);
        mentor.setRole(roleMentor); // Affecter le rôle
        mentorRepository.save(mentor);

        return ResponseEntity.ok("Mentor inscrit avec succès");
    }

    private void copyUserDetails(UserDto dto, UserEntity user) {
        user.setPrenom(dto.getPrenom());
        user.setNom(dto.getNom());
        user.setAdresse(dto.getAdresse());
        user.setDateNaissance(dto.getDateNaissance());
        user.setEmail(dto.getEmail());
        user.setNomUtilisateur(dto.getNomUtilisateur());
        user.setMotDePasse(PasswordUtil.encode(dto.getMotDePasse()));
        user.setPhoto(dto.getPhoto());
        user.setStatut(UserEntity.StatutUser.INACTIF);
    }




















    @Transactional
    public UserEntity save(UserEntity user) {
        // Vérifier si le rôle existe déjà
        RoleEntity role = roleRepository.findByNom(user.getRole().getNom())
                .orElseThrow(() -> new RuntimeException("Le rôle spécifié n'existe pas"));

        // Assigner le rôle existant
        user.setRole(role);

        return userRepository.save(user);
    }

    @Transactional
    public List<UserEntity> saveAll(List<UserEntity> users) {
        // Valide les rôles pour chaque utilisateur
        for (UserEntity user : users) {
            RoleEntity role = roleRepository.findByNom(user.getRole().getNom())
                    .orElseThrow(() -> new RuntimeException("Rôle invalide : " + user.getRole().getNom()));
            user.setRole(role);
        }

        // Sauvegarde tous les utilisateurs après validation
        return userRepository.saveAll(users);
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id); // Utilisation de la méthode fournie par JpaRepository
    }
}
