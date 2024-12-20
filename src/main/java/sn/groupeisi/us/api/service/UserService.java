package sn.groupeisi.us.api.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.config.PasswordUtil;
import sn.groupeisi.us.api.dto.AdminDto;
import sn.groupeisi.us.api.dto.EtudiantDto;
import sn.groupeisi.us.api.dto.MentorDto;
import sn.groupeisi.us.api.dto.UserDto;
import sn.groupeisi.us.api.entity.*;
import sn.groupeisi.us.api.exception.EntityAlreadyExistsException;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.mapper.DisponibiliteMapper;
import sn.groupeisi.us.api.mapper.UserMapper;
import sn.groupeisi.us.api.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final EtudiantRepository etudiantRepository;
    private final MentorRepository mentorRepository;
    private final RoleRepository roleRepository;
    private final FiliereRepository filiereRepository;
    private final DomaineRepository domaineRepository;
    private final DisponibiliteMapper disponibiliteMapper;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       AdminRepository adminRepository,
                       EtudiantRepository etudiantRepository,
                       MentorRepository mentorRepository,
                       RoleRepository roleRepository,
                       FiliereRepository filiereRepository,
                       DomaineRepository domaineRepository,
                       UserMapper userMapper,
                       DisponibiliteMapper disponibiliteMapper) {
        this.userRepository = userRepository;
        this.etudiantRepository = etudiantRepository;
        this.adminRepository = adminRepository;
        this.mentorRepository = mentorRepository;
        this.roleRepository = roleRepository;
        this.filiereRepository = filiereRepository;
        this.domaineRepository = domaineRepository;
        this.userMapper = userMapper;
        this.disponibiliteMapper = disponibiliteMapper;
    }

    @Transactional
    public EtudiantDto registerEtudiant(EtudiantDto etudiantDto) {
        // Vérifie si l'email ou le nom d'utilisateur existe déjà
        checkEmailAndUsername(etudiantDto);

        FiliereEntity filiere = filiereRepository.findById(etudiantDto.getFiliereId())
                .orElseThrow(() -> new EntityNotFoundException("Filière introuvable"));

        RoleEntity roleEtudiant = roleRepository.findByNom("ETUDIANT")
                .orElseThrow(() -> new EntityNotFoundException("Rôle ETUDIANT introuvable"));

        EtudiantEntity etudiant = new EtudiantEntity();
        copyUserDetails(etudiantDto, etudiant);
        etudiant.setFiliere(filiere);
        etudiant.setNiveau(etudiantDto.getNiveau());
        etudiant.setRole(roleEtudiant);
        etudiant.setStatut(UserEntity.StatutUser.ACTIF);

        return toEtudiantDto(etudiantRepository.save(etudiant));
    }

    @Transactional
    public MentorDto registerMentor(MentorDto mentorDto) {
        // Vérifie si l'email ou le nom d'utilisateur existe déjà
        checkEmailAndUsername(mentorDto);

        Set<FiliereEntity> filieres = new HashSet<>(filiereRepository.findAllById(mentorDto.getFiliereIds()));
        Set<DomaineEntity> domaines = new HashSet<>(domaineRepository.findAllById(mentorDto.getDomaineIds()));

        if (filieres.isEmpty()) {
            throw new IllegalArgumentException("Filières invalides");
        }

        RoleEntity roleMentor = roleRepository.findByNom("MENTOR")
                .orElseThrow(() -> new EntityNotFoundException("Rôle MENTOR introuvable"));

        MentorEntity mentor = new MentorEntity();
        copyUserDetails(mentorDto, mentor);
        mentor.setFilieres(filieres);
        mentor.setDomaines(domaines);
        mentor.setDescription(mentorDto.getDescription());
        mentor.setRole(roleMentor);
        mentor.setStatut(UserEntity.StatutUser.INACTIF); // Statut inactif par défaut

        MentorEntity savedMentor = mentorRepository.save(mentor);

        // Créer une demande d'adhésion associée
        DemandeAdhesionEntity demandeAdhesion = new DemandeAdhesionEntity();
        demandeAdhesion.setDateDemande(LocalDateTime.now());
        demandeAdhesion.setMentor(savedMentor);
        demandeAdhesion.setStatut(DemandeAdhesionEntity.StatutDemande.EN_ATTENTE);

        savedMentor.setDemandeAdhesion(demandeAdhesion);
        mentorRepository.save(savedMentor); // Sauvegarder à nouveau avec la demande associée

        return toMentorDto(savedMentor);
    }

    // Méthode pour enregistrer un administrateur
    @Transactional
    public AdminDto registerAdmin(AdminDto adminDto) {
        // Vérifie si l'email ou le nom d'utilisateur existe déjà
        checkEmailAndUsername(adminDto);

        RoleEntity roleAdmin = roleRepository.findByNom("ADMIN")
                .orElseThrow(() -> new EntityNotFoundException("Rôle ADMIN introuvable"));

        AdminEntity admin = new AdminEntity();
        copyUserDetails(adminDto, admin);
        admin.setRole(roleAdmin);
        admin.setPermissions("FULL_ACCESS");
        admin.setStatut(UserEntity.StatutUser.ACTIF);

        return toAdminDto(adminRepository.save(admin));
    }

    // Méthode générique pour copier les champs communs d'un utilisateur
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

    /**
     * Récupérer un utilisateur par ID.
     */
    public UserEntity getUserDetailsById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'ID " + id + " introuvable"));
    }

    /**
     * Récupérer tous les utilisateurs.
     */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::copyUserDetailsToDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer tous les étudiants.
     */
    public List<EtudiantDto> getAllStudents() {
        return etudiantRepository.findAll().stream()
                .map(this::toEtudiantDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer tous les mentors.
     */
    public List<MentorDto> getAllMentors() {
        return mentorRepository.findAll().stream()
                .map(this::toMentorDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer tous les administrateurs.
     */
    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::toAdminDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateUserRole(Long userId, String roleName) {
        // Récupérer l'utilisateur
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'ID " + userId + " introuvable"));

        // Récupérer le rôle
        RoleEntity role = roleRepository.findByNom(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Rôle '" + roleName + "' introuvable"));

        // Mettre à jour le rôle
        user.setRole(role);

        // Sauvegarder les modifications
        UserEntity updatedUser = userRepository.save(user);

        // Convertir l'entité mise à jour en DTO
        UserDto dto = new UserDto();
        return copyUserDetailsToDto(updatedUser, dto);
    }

    /**
     * Supprimer un utilisateur.
     */
    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'ID " + id + " introuvable"));
        userRepository.delete(user);
    }

    public AdminDto toAdminDto(AdminEntity adminEntity) {

        AdminDto adminDto = new AdminDto();
        copyUserDetailsToDto(adminEntity, adminDto);
        adminDto.setPermissions(adminEntity.getPermissions());

        return adminDto;
    }

    public EtudiantDto toEtudiantDto(EtudiantEntity etudiantEntity) {

        EtudiantDto etudiantDto = new EtudiantDto();
        copyUserDetailsToDto(etudiantEntity, etudiantDto);
        etudiantDto.setFiliereId(etudiantEntity.getFiliere().getId());
        etudiantDto.setNiveau(etudiantEntity.getNiveau());

        return etudiantDto;
    }

    public MentorDto toMentorDto(MentorEntity mentorEntity) {

        MentorDto mentorDto = new MentorDto();
        copyUserDetailsToDto(mentorEntity, mentorDto);

        // Gérer les collections nulles
        // Mapper les IDs des filières
        mentorDto.setFiliereIds(Optional.ofNullable(mentorEntity.getFilieres())
                .orElse(Collections.emptySet())
                .stream()
                .map(FiliereEntity::getId)
                .collect(Collectors.toSet()));

        // Mapper les IDs des domaines
        mentorDto.setDomaineIds(Optional.ofNullable(mentorEntity.getDomaines())
                .orElse(Collections.emptySet())
                .stream()
                .map(DomaineEntity::getId)
                .collect(Collectors.toSet()));

        /*mentorDto.setFiliereIds(mentorEntity.getFilieres() != null
                ? mentorEntity.getFilieres().stream().map(FiliereEntity::getId).collect(Collectors.toSet())
                : new HashSet<>());

        mentorDto.setDomaineIds(mentorEntity.getDomaines() != null
                ? mentorEntity.getDomaines().stream().map(DomaineEntity::getId).collect(Collectors.toSet())
                : new HashSet<>());*/

        mentorDto.setDisponibilite(disponibiliteMapper.toDto(mentorEntity.getDisponibilite()));
        mentorDto.setDescription(mentorEntity.getDescription());
        mentorDto.setSpecialite(mentorEntity.getSpecialite());

        return mentorDto;
    }

    /**
     * Obtenir le rôle d'un utilisateur par son ID.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @return Le rôle de l'utilisateur sous forme de chaîne.
     */
    public String getRoleByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + userId));

        if (user.getRole() == null) {
            throw new EntityNotFoundException("Aucun rôle associé à l'utilisateur avec l'ID : " + userId);
        }

        return user.getRole().getNom(); // Retourne le nom du rôle
    }

    /**
     * Récupérer tous les étudiants sous la tutelle d'un mentor.
     *
     * @param mentorId L'identifiant du mentor.
     * @return Liste des détails des étudiants.
     */
    // A COMPLETER

    public UserDto copyUserDetailsToDto(UserEntity userEntity, UserDto userDto) {

        System.out.println("Le role du UTILISATEUR: "+userEntity.getRole().getNom());
        userDto.setId(userEntity.getId());
        userDto.setPrenom(userEntity.getPrenom());
        userDto.setNom(userEntity.getNom());
        userDto.setAdresse(userEntity.getAdresse());
        userDto.setDateNaissance(userEntity.getDateNaissance());
        userDto.setMotDePasse(userEntity.getMotDePasse());
        userDto.setPhoto(userEntity.getPhoto());
        userDto.setEmail(userEntity.getEmail());
        userDto.setNomUtilisateur(userEntity.getNomUtilisateur());
        userDto.setRoleNom(userEntity.getRole().getNom());

        // Vérifiez si le statut est nul
        /*if (userEntity.getStatut() != null) {
            userDto.setStatut(userEntity.getStatut().toString());
        } else {
            userDto.setStatut("INDEFINI"); // Valeur par défaut ou gérer autrement
        }*/

        // Gestion du statut avec une valeur par défaut
        userDto.setStatut(Optional.ofNullable(userEntity.getStatut())
                .map(Enum::toString)
                .orElse("INDEFINI"));

        return userDto;
    }

    // Surcharge de la methode copyUserDetailsToDto
    public UserDto copyUserDetailsToDto(UserEntity userEntity) {
        // Appelle la version avec un paramètre UserDto et instancie un nouvel objet par défaut
        return copyUserDetailsToDto(userEntity, new UserDto());
    }

    // Vérifie si l'email ou le nom d'utilisateur existe déjà
    private void checkEmailAndUsername(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EntityAlreadyExistsException("Un utilisateur avec cet email existe déjà : " + userDto.getEmail());
        }
        if (userRepository.existsByNomUtilisateur(userDto.getNomUtilisateur())) {
            throw new EntityAlreadyExistsException("Un utilisateur avec ce nom d'utilisateur existe déjà : " + userDto.getNomUtilisateur());
        }
    }

    public List<Object> getAllUsersAsDtos() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    System.out.println("\nTraitement\n");
                    if (user instanceof EtudiantEntity etudiant) {
                        System.out.println("\nEtudiant\n");
                        return toEtudiantDto(etudiant);
                    } else if (user instanceof MentorEntity mentor) {
                        System.out.println("\nMentor\n");
                        return toMentorDto(mentor);
                    } else if (user instanceof AdminEntity admin) {
                        System.out.println("\nAdmin\n");
                        return toAdminDto(admin);
                    } else {
                        return copyUserDetailsToDto(user);
                    }
                })
                .collect(Collectors.toList());
    }


    /* *********************************************************************
        RECHERCHE
     * *********************************************************************/


    /**
     * Rechercher des mentors selon plusieurs critères.
     *
     * @param nom     Nom ou prénom du mentor.
     * @param statut  Statut de disponibilité (ouvert, plein, indisponible).
     * @param filiere Nom de la filière.
     * @param domaine Nom du domaine.
     * @return Liste des mentors correspondants sous forme de DTO.
     */
    public List<MentorDto> rechercherMentors(String nom, String statut, String filiere, String domaine) {
        List<MentorEntity> mentors = mentorRepository.rechercherMentors(nom, statut, filiere, domaine);

        if (mentors.isEmpty()) {
            throw new EntityNotFoundException("Aucun mentor trouvé pour les critères spécifiés.");
        }

        // Convertir les entités en DTOs
        return mentors.stream()
                .map(this::toMentorDto)
                .toList();
    }
}
