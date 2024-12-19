package sn.groupeisi.us.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.groupeisi.us.api.dto.RoleDto;
import sn.groupeisi.us.api.entity.RoleEntity;
import sn.groupeisi.us.api.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Ajouter un nouveau rôle
    @PostMapping
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDto) {
        RoleDto savedRole = roleService.addRole(roleDto);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    // Récupérer un rôle par ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        RoleDto roleDto = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDto);
    }

    // Récupérer un rôle par nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<RoleDto> getRoleByNom(@PathVariable String nom) {
        RoleDto roleDto = roleService.getRoleByNom(nom);
        return ResponseEntity.ok(roleDto);
    }

    // Récupérer tous les rôles
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    // Mettre à jour un rôle existant
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(updatedRole);
    }

    // Supprimer un rôle par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
