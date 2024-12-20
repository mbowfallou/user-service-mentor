package sn.groupeisi.us.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.AdminDto;
import sn.groupeisi.us.api.dto.EtudiantDto;
import sn.groupeisi.us.api.dto.MentorDto;
import sn.groupeisi.us.api.dto.UserDto;
import sn.groupeisi.us.api.entity.AdminEntity;
import sn.groupeisi.us.api.entity.EtudiantEntity;
import sn.groupeisi.us.api.entity.MentorEntity;
import sn.groupeisi.us.api.entity.UserEntity;
import sn.groupeisi.us.api.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Record a Student
    @PostMapping("/etudiant")
    public ResponseEntity<EtudiantDto> registerEtudiant(@RequestBody EtudiantDto etudiantDto) {
        EtudiantDto savedEtudiant = userService.registerEtudiant(etudiantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEtudiant);
    }

    // Record a Mentor
    @PostMapping("/mentor")
    public ResponseEntity<MentorDto> registerMentor(@RequestBody MentorDto mentorDto) {
        MentorDto savedMentor = userService.registerMentor(mentorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMentor);
    }

    // Record an Admin
    @PostMapping("/admin")
    public ResponseEntity<AdminDto> registerMentor(@RequestBody AdminDto adminDto) {
        AdminDto savedAdmin = userService.registerAdmin(adminDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }

    // Retrieve a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserBy_Id(@PathVariable Long id) {
        // Récupère l'utilisateur via le service
        UserEntity user = userService.getUserDetailsById(id);

        // Identifie le type d'utilisateur en fonction de son rôle ou de sa classe
        switch (user.getRole().getNom().toUpperCase()) {
            case "ETUDIANT":
                return ResponseEntity.ok(userService.toEtudiantDto((EtudiantEntity) user));
            case "MENTOR":
                return ResponseEntity.ok(userService.toMentorDto((MentorEntity) user));
            case "ADMIN":
                return ResponseEntity.ok(userService.toAdminDto((AdminEntity) user));
            default:
                // Retourne un UserDto générique si le rôle ne correspond pas
                return ResponseEntity.ok(userService.copyUserDetailsToDto(user));
        }
    }

    // Retrieve all Users
    @GetMapping
    public ResponseEntity<List<?>> getAllUsers2() {
        List<Object> userDtos = userService.getAllUsersAsDtos();
        return ResponseEntity.ok(userDtos);
    }

    // Retrieve an Administrator
    @GetMapping("/admins")
    public ResponseEntity<List<AdminDto>> getAdmins() {
        List<AdminDto> userDtos = userService.getAllAdmins();
        return ResponseEntity.ok(userDtos);
    }

    // Retrieve all Students
    @GetMapping("/students")
    public ResponseEntity<List<EtudiantDto>> getAllStudents() {
        List<EtudiantDto> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // Retrieve all Mentors
    @GetMapping("/mentors")
    public ResponseEntity<List<MentorDto>> getAllMentors() {
        List<MentorDto> mentors = userService.getAllMentors();
        return ResponseEntity.ok(mentors);
    }

    // Change the Role of a User
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @RequestParam String roleName) {
        UserDto updatedUser = userService.updateUserRole(id, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Update a User
    /*@PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }
    */




    /**
     * Endpoint pour rechercher des mentors selon plusieurs critères.
     *
     * @param nom     Nom ou prénom du mentor.
     * @param statut  Statut de disponibilité (ouvert, plein, indisponible).
     * @param filiere Nom de la filière.
     * @param domaine Nom du domaine.
     * @return Liste des mentors correspondants sous forme de DTO.
     */
    @GetMapping("/search")
    public ResponseEntity<List<MentorDto>> rechercherMentors(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String domaine
    ) {
        List<MentorDto> mentors = userService.rechercherMentors(nom, statut, filiere, domaine);
        return ResponseEntity.ok(mentors);
    }
}
