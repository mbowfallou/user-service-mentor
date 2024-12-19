package sn.groupeisi.us.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.groupeisi.us.api.service.RoleService;

@Configuration
public class RoleInitializer {

    private final RoleService roleService;

    public RoleInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Bean
    public CommandLineRunner initializeRoles() {
        return args -> {
            try {
                roleService.saveRoleIfNotExists("ADMIN", "Administrateur du système");
                roleService.saveRoleIfNotExists("USER", "Utilisateur standard");
                roleService.saveRoleIfNotExists("ETUDIANT", "Étudiant inscrit");
                roleService.saveRoleIfNotExists("MENTOR", "Mentor accompagnateur");
                System.out.println("Les rôles ont été initialisés avec succès !");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation des rôles : " + e.getMessage());
            }
        };
    }
}
