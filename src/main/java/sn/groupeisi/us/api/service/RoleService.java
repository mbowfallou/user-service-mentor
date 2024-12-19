package sn.groupeisi.us.api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.dto.RoleDto;
import sn.groupeisi.us.api.entity.RoleEntity;
import sn.groupeisi.us.api.exception.EntityAlreadyExistsException;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.mapper.RoleMapper;
import sn.groupeisi.us.api.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    // Créer un nouveau rôle
    public RoleDto addRole(RoleDto roleDto) {
        // Vérifie si un rôle avec le même nom existe déjà
        roleRepository.findByNom(roleDto.getNom()).ifPresent(role -> {
            throw new EntityAlreadyExistsException("Le rôle '" + roleDto.getNom() + "' existe déjà.");
        });

        RoleEntity roleEntity = roleMapper.toEntity(roleDto);
        RoleEntity savedRole = roleRepository.save(roleEntity);
        return roleMapper.toDto(savedRole);
    }

    // Récupérer un rôle par ID
    public RoleDto getRoleById(Long roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rôle avec l'ID '" + roleId + "' introuvable."));
        return roleMapper.toDto(roleEntity);
    }

    // Récupérer un rôle par nom
    public RoleDto getRoleByNom(String nom) {
        RoleEntity roleEntity = roleRepository.findByNom(nom)
                .orElseThrow(() -> new EntityNotFoundException("Rôle avec le nom '" + nom + "' introuvable."));
        return roleMapper.toDto(roleEntity);
    }

    // Récupérer tous les rôles
    public List<RoleDto> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    // Mettre à jour un rôle existant
    public RoleDto updateRole(Long roleId, RoleDto roleDto) {
        RoleEntity existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rôle avec l'ID '" + roleId + "' introuvable."));

        // Vérifie si le nouveau nom existe déjà pour un autre rôle
        roleRepository.findByNom(roleDto.getNom()).ifPresent(role -> {
            if (!role.getId().equals(roleId)) {
                throw new EntityAlreadyExistsException("Le rôle '" + roleDto.getNom() + "' existe déjà.");
            }
        });

        // Mise à jour des champs
        existingRole.setNom(roleDto.getNom());
        RoleEntity updatedRole = roleRepository.save(existingRole);
        return roleMapper.toDto(updatedRole);
    }

    // Supprimer un rôle
    public void deleteRole(Long roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rôle avec l'ID '" + roleId + "' introuvable."));
        roleRepository.delete(roleEntity);
    }


    // UTIL
    public void saveRoleIfNotExists(String nom, String description) {
        roleRepository.findByNom(nom).ifPresentOrElse(
                existingRole -> {
                    System.out.println("Le rôle '" + nom + "' existe déjà.");
                },
                () -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setNom(nom);
                    roleDto.setDescription(description);
                    addRole(roleDto);
                    System.out.println("Le rôle '" + nom + "' a été ajouté.");
                }
        );
    }

}
