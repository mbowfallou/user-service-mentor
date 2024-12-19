package sn.groupeisi.us.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.UserDto;
import sn.groupeisi.us.api.entity.UserEntity;
import sn.groupeisi.us.api.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PostMapping("/save")
    public UserEntity saveUser(@RequestBody UserEntity user) {
        return userService.save(user);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<List<UserEntity>> saveAllUsers(@RequestBody List<UserEntity> users) {
        List<UserEntity> savedUsers = userService.saveAll(users);
        return ResponseEntity.ok(savedUsers);
    }

    // Récupérer un utilisateur par ID (pour vérifier son existence)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(404).body("Utilisateur avec l'ID " + id + " introuvable.");
        }
    }

    @GetMapping("/{id}/role")
    public ResponseEntity<String> getUserRole(@PathVariable Long id) {
        Optional<UserEntity> user = userService.findById(id);
        return user.map(userEntity -> ResponseEntity.ok(userEntity.getRole().getNom())).orElseGet(() -> ResponseEntity.status(404).body("Utilisateur avec l'ID " + id + " introuvable."));

    }

}
